from utils import *

#import time
import time as _time
from time import sleep
import datetime
import os
from os.path import isfile
import ast
from shutil import copyfile, copy
import sys
from timeit import default_timer as timer

import numpy as np
from numpy import array, random, arange, float32, float64, zeros
import pandas as pd

import librosa
import librosa.display
from librosa.feature import melspectrogram
#import pyaudio
#from pyaudio import PyAudio, paContinue, paFloat32
import sounddevice as sd

from collections import Counter
#import OSC
import pythonosc
# Code to run the AI VJ. currently v2
# callback loop seems to cycle 10x /sec

############################
#     Constants
############################

pattern_osc_route = '/lx/channel/1/activePattern'

pattern_1_osc_route = '/lx/channel/1/activePattern'
pattern_2_osc_route = '/lx/channel/2/activePattern'

pattern_3_osc_route = '/lx/channel/3/activePattern'
pattern_4_osc_route = '/lx/channel/4/activePattern'
color_osc_route = '/lx/palette/color/hue'
speed_osc_route = '/lx/engine/speed'
blur_osc_route = '/lx/channel/1/effect/1/amount/'
desat_osc_route = '/lx/channel/1/effect/2/amount/'
blur_osc_boolean = '/lx/channel/1/effect/1/enabled/'
desat_osc_boolean = '/lx/channel/1/effect/2/enabled/'

brightness_osc_route = '/lx/output/brightness'
noise_density_osc_route = '/lx/channel/1/pattern/10/Dens/' #default 0.7
cubeflash_speed_osc_route = '/lx/channel/1/pattern/9/RATE/' #default 0.3
pattern_chan2_osc_route = '/lx/channel/2/activePattern'
output_on_osc_route = '/lx/output/enabled'
transition_osc_route = '/lx/channel/1/transitionEnabled'
transition_duration_osc_route = '/lx/channel/1/transitionTimeSecs'

#WEIGHTS_FOLDER = '/Users/aaronopp/Desktop/SymmetryLabs/ML_model/model_weights/v2/'

AI_VJ_FOLDER = sys.argv[0][:-6]
WEIGHTS_FOLDER = AI_VJ_FOLDER + 'model_weights/v2/'
TRAINING_DATA_FOLDER = AI_VJ_FOLDER + 'training_data/'
DATA_FOLDER = TRAINING_DATA_FOLDER + sys.argv[1] + '/'


patterns_full = ['AskewPlanes', 'Balance', 'Ball', 'BassPod', 'Blank', 'Bubbles', 'CrossSections', 'CubeEQ', 
                 'CubeFlash', 'Noise', 'Palette', 'Pong', 'Rings', 'ShiftingPlane', 'SoundParticles', 'SpaceTime',
                'Spheres', 'StripPlay', 'Swarm', 'Swim', 'TelevisionStatic', 'Traktor', 'ViolinWave']

patterns_reduced =  ['AskewPlanes', 'Balance', 'CrossSections', 'CubeEQ', 
                 'CubeFlash', 'Noise', 'Pong', 'Rings', 'ShiftingPlane', 'SoundParticles', 'SpaceTime',
                'Spheres', 'StripPlay', 'Swarm', 'Swim', 'Traktor', 'ViolinWave']


if (len(sys.argv) < 3 or  len(sys.argv) > 3): 
    print('need 2 args, first is your name, second is run time in min!')
    sys.exit(1)

set_sounddevices(sd)
print(sd.query_devices())
sd.default.channels = 1

print('default devices', sd.default.device)

############################
# initiate OSC client
############################

#c = OSC.OSCClient()
#c.connect(('0.0.0.0', 3030))   

i = 0
z = 0

X_train_shape = np.array((1765, 1, 96, 938))

Y_pattern_train_shape = np.array((1765, 1, 96, 938))
Y_color_train_shape = np.array((1765, 1, 96, 938)) #np.load(DATA_FOLDER + 'Y_color_train_v2.npy')
Y_speed_train_shape = np.array((1765, 1, 96, 938)) #np.load(DATA_FOLDER + 'Y_speed_train_v2.npy')

X_train_small_shape = np.array((856, 1, 96, 313)) #np.load(DATA_FOLDER + 'X_train_small_v2.npy')
Y_blur_train_shape = np.array((856, 1, 96, 313)) #np.load(DATA_FOLDER + 'Y_blur_v2.npy')
Y_desat_train_shape = np.array((856, 1, 96, 313)) #np.load(DATA_FOLDER + 'Y_desat_v2.npy')

Y_speed_classes = 3
Y_color_classes = 8
Y_pattern_classes = 24
Y_effect_classes = 4

print('checking shapes')
print('pattern shape: ', Y_pattern_train_shape)
print('color shape: ', Y_color_train_shape)
print('speed shape: ', Y_speed_train_shape)

print('comparing all lengths!')
print('x test trimmed shape: ', X_train_shape)

print('x test trimmed shape: ', X_train_small_shape)
#print 'Y_param shape: ', Y_param_full.shape
print('Y_blur shape: ', Y_blur_train_shape)
print('Y_desat shape: ', Y_desat_train_shape)

############################
# build and load models
############################

# big models, 15 sec

model_speed = build_model_linear_end(X_train_shape, Y_speed_train_shape, nb_classes= Y_speed_classes)          
model_color = build_model_linear_end(X_train_shape, Y_color_train_shape, nb_classes= Y_color_classes)
model_pattern = build_model_linear_end_pattern(X_train_shape, Y_pattern_train_shape, nb_classes = Y_pattern_classes)

# v2 MODELS
# small models (5sec)

model_blur = build_model_linear_end(X_train_small_shape, Y_blur_train_shape, nb_classes=Y_effect_classes)
model_desat = build_model_linear_end(X_train_small_shape, Y_desat_train_shape, nb_classes=Y_effect_classes)

# load weights (stable v2)

model_speed.load_weights(WEIGHTS_FOLDER + 'weights_speed_11_2_noval_85.hdf5')
model_color.load_weights(WEIGHTS_FOLDER + 'weights_color_11_2_noval_91.hdf5')
model_pattern.load_weights(WEIGHTS_FOLDER + 'weights_pattern_11_2_noval_84.hdf5')
model_blur.load_weights(WEIGHTS_FOLDER + 'weights_blur_11_2_noval_84.hdf5')
model_desat.load_weights(WEIGHTS_FOLDER + 'weights_desat_11_2_noval_80.hdf5')

###################################
#      default osc messages
###################################

send_osc(pattern_1_osc_route, 3)
send_osc(pattern_2_osc_route, 3)
send_osc(pattern_3_osc_route, 3)
send_osc(pattern_4_osc_route, 3)
send_osc(noise_density_osc_route, 0.8)
send_osc(cubeflash_speed_osc_route, 0.05)
send_osc(output_on_osc_route, 1)
send_osc(transition_osc_route, 1)
send_osc(transition_duration_osc_route, 0.01)

###################################
#       Running test model
###################################

# Audio data size of 15 sec and sample rate every 5 sec.
# For sample size of 15 sec, mel size is 938.

duration = 15 # seconds
run_time_min = int(sys.argv[2])
run_time = run_time_min*60

print('running for ' + str(run_time_min) + ' minutes!')

x = np.array([],ndmin = 2)
mel_size = 938 # 1872 for 30 sec
RATE = 16000
start = _time.time()

b = Buffer(duration * RATE)
#b_small = Buffer(short_term_duration * RATE)
color_old = 0

#print 'b read', b.read()

X_test = np.zeros((100, 1, 96, mel_size))  # or could be run_time/5 sec
time_test = np.zeros((100))


def callback(indata, frames, time, status): #outdata is 5th - when no inputstream


    global i
    global z
    global mel_size
    global run_time
    global speed_labels
    global color_labels_encoding
    global patterns_full, speed_unencode, color_unencode
    global pattern_osc_route, color_osc_route, speed_osc_route
    if status:
        print(status)
    
    #print 'indata:', indata
    #print 'avg: ', indata.mean()
    b.extend(indata.squeeze())
    #b_small.extend(indata.squeeze())
    elapsed_time = _time.time()- start
    #callback_time_array[i] = time2.time()
    
    if elapsed_time > duration and i % 50 == 0:
        
        print('time elapsed:', elapsed_time)
        
        aud = b.read()
        Spec = get_mel_spectrogram(aud)
        Spec = Spec[:,:,:,0:mel_size]
        
        Spec_small = Spec[:, :, :, -313:]
        if Spec[:, :, :, -300:].mean() < -98.0:
             print('music off')
             send_osc(pattern_osc_route, 4)
        else:
            speed_predict = model_speed.predict(Spec)
            color_predict = model_color.predict(Spec)
            pattern_predict = model_pattern.predict(Spec)
            print('pattern predict shape!', pattern_predict.shape)
            blur_predict = model_blur.predict(Spec_small)
            desat_predict = model_desat.predict(Spec_small)

            #print 'max data pos: ' , speed_predict.argmax()
            speed_label, speed_index = get_max_label(speed_predict, speed_labels)
            color_label, color_index = get_max_label(color_predict, color_labels_encoding)
            pattern_label, pattern_index = get_max_label(pattern_predict, patterns_full)
            
            blur_label, blur_index = get_max_label(blur_predict, effect_labels_full)  
            desat_label, desat_index = get_max_label(desat_predict, effect_labels_full)  
            
            print('speed: ', speed_label)
            print('color: ', color_label)
            print('pattern: ', pattern_label)
            print('blur: ', blur_label)
            print('desat: ', desat_label)
            print('PATTERN MAX READING: ', pattern_predict.max())
            
            if (pattern_predict.max() > 0.40):
                print('pattern osc route: ', pattern_osc_route, type(pattern_osc_route))
                print('pattern index: ', pattern_index, type(pattern_index))
                send_osc(pattern_osc_route, int(pattern_index))
            
            send_osc(color_osc_route, color_unencode[color_index])
            send_osc(speed_osc_route, speed_unencode[speed_index])
            send_osc(blur_osc_route, effect_unencode[blur_index])
            send_osc(desat_osc_route, effect_unencode[desat_index])
            
            color_old = color_index
       
        z += 1

    i += 1

with sd.InputStream(samplerate=16000, dtype= np.float32, channels=1, callback=callback):
   
    sd.sleep(int(run_time*1000))
    #np.save('x_test_sd_9_21.npy', X_test)
    #print 'callback times'
    #for i in range(5, 50):
        #print callback_time_array[i] - callback_time_array[i-1]
    print('saved!')

