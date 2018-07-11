import sys

import spotipy
import spotipy.util as util


import calendar
import dateutil
import json
import pprint

import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import os

#scope = 'user-library-read'
#scope = 'user-top-read user-read-playback-state'
# scope = 'user-read-playback-state user-read-recently-played'

# token = util.prompt_for_user_token('aaronopp', scope, client_id='dbe2a20785304190b8e35d5d6644397b', client_secret='cc259d8378be48beaad9171a5afb19ba', redirect_uri='http://localhost:8888/callback')
# import dateutil.parser as dp



# just realized i should only create/save features at the end when i have all ids. then i
# only execute it when i need it!
def spotify_pipeline(token, scope, time_test_scaled, filename):

	spotify_data_df = create_spotify_df(token, scope, time_test_scaled)
	spotify_data_df.to_csv(filename + '_raw.csv')
	print spotify_data_df.index[1]

	print time_test_scaled[1:4]
	print spotify_data_df.index[1]- time_test_scaled[1]
	spotify_dataset = create_spotify_dataset(spotify_data_df, time_test_scaled)
	spotify_dataset.to_csv(filename + '.csv')
	return spotify_dataset
def save_spotify_df(token, scope, filename):
	
    spotify_data_df = create_spotify_df(token, scope)
    spotify_data_df.to_csv(filename)

def create_spotify_df(token, scope, get_genre=True, save_features=False):
    timestamp = []
    songs = []
    artists = []
    popularities = []
    ids = []
    artist_ids = []
    genres = []
    if token:
        sp = spotipy.Spotify(auth=token)
        sp.trace=False
        ranges = ['short_term', 'medium_term', 'long_term']
        #esults = sp.current_user_playing_track()
        curr_results = sp.current_playback()
        #print curr_results
        #print 'current playback keys ', curr_results.keys()
        #print results['item']
        #print 'user playing track keys', results.keys()
        print 'song name: ', curr_results['item']['name']
        print 'timestamp: ', curr_results['timestamp']
        curr_artist_info = curr_results['item']['artists']
        print 'artist name: ', curr_artist_info[0]['name']
        print 'artist id: ', curr_artist_info[0]['id']



        #print curr_results['timestamp']
        #print 'type name: ', curr_results['item']['type']
        print 'song id: ', curr_results['item']['id']
        
        print '\n starting spotify data with current playing song! \n'

        timestamp.append(str(curr_results['timestamp']).encode('utf-8'))
        songs.append(curr_results['item']['name'].encode('utf-8'))
        ids.append(curr_results['item']['id'].encode('utf-8'))

        artists.append(curr_artist_info[0]['name'].encode('utf-8'))
        artist_ids.append(curr_artist_info[0]['id'].encode('utf-8'))

        popularities.append(curr_results['item']['popularity'])
        #if get_genre == True:
        genres.append(get_genre_from_track_id(sp, curr_results['item']['id'].encode('utf-8')))
                
        recent_results = sp.current_user_recently_played(limit=5)
        #print recent_results.keys()

        #print recent_results['href']
        #print recent_results['cursors']
        #print recent_results['next']

        for i, item in enumerate(recent_results['items']):
            print 'name: ', item['track']['name']
            print 'id: ', item['track']['id']
            artist_info = item['track']['artists']
            print 'artist: ', artist_info[0]['name']
            print 'duration ms: ', item['track']['duration_ms']

            print 'popularity: ', item['track']['popularity']


            print 'played: ', item['played_at']
            print 'type', type(item['played_at'])
            timestamp.append(item['played_at'].encode('utf-8'))
            songs.append(item['track']['name'].encode('utf-8'))
            ids.append(item['track']['id'].encode('utf-8'))

            artists.append(artist_info[0]['name'].encode('utf-8'))
            artist_ids.append(artist_info[0]['id'].encode('utf-8'))

            popularities.append(item['track']['popularity'])
            #if get_genre == True:
            genres.append(get_genre_from_track_id(sp, item['track']['id'].encode('utf-8')))
                
        #get timestamps
        utc_timestamps = []
        for index, time in enumerate(timestamp):
            if index != 0:
                utc_timestamps.append(calendar.timegm(dateutil.parser.parse(time).timetuple())*1000)
            else:
                utc_timestamps.append(time)
        print utc_timestamps
        # create dataframe of basic data.
        spotify_data = {'timestamp': utc_timestamps, 'artist': artists, 'artist_id': artist_ids, 'song': songs, 'id': ids, 'genre': genres, 'popularity': popularities}
        spotify_data_df = pd.DataFrame(spotify_data)
        spotify_data_df = spotify_data_df.set_index('timestamp')
        
        if save_features == True:
            try: 
                save_spotify_features(sp, ids)
                print 'features saved!'
            except:
                print 'unable to save features'

    return spotify_data_df

def save_spotify_features(sp, ids):
    # get audio features for each track and save as a JSON!
    features = sp.audio_features(ids)
    features_json = []
    for feature in features:
        #print feature
        print(json.dumps(feature, indent=4))
        print()
        features_json.append(json.dumps(feature, indent=4))
        with open('data.json', 'a') as outfile:
            json.dump(feature, outfile, indent=4)
        #analysis = sp._get(feature['analysis_url'])
        #print(json.dumps(analysis, indent=4))
        #print()
def get_album_id_from_track(sp, id):
    track = sp.track(id)
    #pprint.pprint(track)
    album_id = track['album']['id']
    
    print album_id
    return album_id
def get_genre_from_track_id(sp, id):
    track = sp.track(id)
    #pprint.pprint(track)
    album_id = track['album']['id']
    album = sp.album(str(album_id))
    pprint.pprint(album['genres'])
    return album['genres']

def get_audio_features_json(sp, dataframe, savefile):
    # get audio features for each track and save as a JSON!
    ids = spotify_data['id'].tolist()
    features = sp.audio_features(ids)
    features_json = []
    for feature in features:
        #print feature
        print(json.dumps(feature, indent=4))
        print()
        features_json.append(json.dumps(feature, indent=4))
        with open(savefile, 'a') as outfile:
            json.dump(feature, outfile, indent=4)
        # OPTIONAL song analysis (too much irrelavant data ATM.)    
        
        #analysis = sp._get(feature['analysis_url'])
        #print(json.dumps(analysis, indent=4))
        #print()

####################################################
#  Functions to match song data with timestamp data
#
####################################################
def match_spotify_dataset(dataframe, time_test_scaled):
    j = 0
    Y_logger_df = pd.DataFrame(data=None, columns=dataframe.columns)

    for index_logger, timestamps in enumerate(time_test_scaled):
        for index, value in enumerate(list(dataframe.index)):
            if value < timestamps:
                print value-timestamps
                print index
                print dataframe.iloc[index]
                Y_logger_df = Y_logger_df.append(dataframe.iloc[index])
                j += 1
                break
    print 'j:' , j
    return Y_logger_df

def create_spotify_dataset(dataframe, time_test_scaled, save_df=False):
    y_df = match_spotify_dataset(dataframe, time_test_scaled)
    y_df['timestamps'] = time_test_scaled
    y_df = y_df.reset_index()
    y_df = y_df.rename(columns={'index': 'song_start_ts'})
    y_df = y_df.set_index('timestamps')
    if save_df == True:
        y_df.to_csv('spotify_dataset.csv')
    return y_df

# to write- get all IDs from big pandas dataframe and then save spotify features
# save spotify data df as dataframe? or just sync it w training data.