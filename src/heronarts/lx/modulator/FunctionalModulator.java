/**
 * Copyright 2013- Mark C. Slee, Heron Arts LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author Mark C. Slee <mark@heronarts.com>
 */

package heronarts.lx.modulator;

import heronarts.lx.parameter.FixedParameter;
import heronarts.lx.parameter.LXParameter;

/**
 * An extendable modulator class that lets a custom normalized function be
 * supplied by simply extending this class and supplying a compute() and
 * invert() method.
 */
public abstract class FunctionalModulator extends LXRangeModulator {

    public FunctionalModulator(double startValue, double endValue, double periodMs) {
        this(new FixedParameter(startValue), new FixedParameter(endValue),
                new FixedParameter(periodMs));
    }

    public FunctionalModulator(LXParameter startValue, double endValue,
            double periodMs) {
        this(startValue, new FixedParameter(endValue), new FixedParameter(periodMs));
    }

    public FunctionalModulator(double startValue, LXParameter endValue,
            double periodMs) {
        this(new FixedParameter(startValue), endValue, new FixedParameter(periodMs));
    }

    public FunctionalModulator(double startValue, double endValue,
            LXParameter periodMs) {
        this(new FixedParameter(startValue), new FixedParameter(endValue), periodMs);
    }

    public FunctionalModulator(LXParameter startValue, LXParameter endValue,
            double periodMs) {
        this(startValue, endValue, new FixedParameter(periodMs));
    }

    public FunctionalModulator(LXParameter startValue, double endValue,
            LXParameter periodMs) {
        this(startValue, new FixedParameter(endValue), periodMs);
    }

    public FunctionalModulator(double startValue, LXParameter endValue,
            LXParameter periodMs) {
        this(new FixedParameter(startValue), endValue, periodMs);
    }

    public FunctionalModulator(LXParameter startValue, LXParameter endValue,
            LXParameter periodMs) {
        this("SIN", startValue, endValue, periodMs);
    }

    public FunctionalModulator(String label, double startValue, double endValue,
            double periodMs) {
        this(label, new FixedParameter(startValue), new FixedParameter(endValue),
                new FixedParameter(periodMs));
    }

    public FunctionalModulator(String label, LXParameter startValue,
            double endValue, double periodMs) {
        this(label, startValue, new FixedParameter(endValue), new FixedParameter(
                periodMs));
    }

    public FunctionalModulator(String label, double startValue,
            LXParameter endValue, double periodMs) {
        this(label, new FixedParameter(startValue), endValue, new FixedParameter(
                periodMs));
    }

    public FunctionalModulator(String label, double startValue, double endValue,
            LXParameter periodMs) {
        this(label, new FixedParameter(startValue), new FixedParameter(endValue),
                periodMs);
    }

    public FunctionalModulator(String label, LXParameter startValue,
            LXParameter endValue, double periodMs) {
        this(label, startValue, endValue, new FixedParameter(periodMs));
    }

    public FunctionalModulator(String label, LXParameter startValue,
            double endValue, LXParameter periodMs) {
        this(label, startValue, new FixedParameter(endValue), periodMs);
    }

    public FunctionalModulator(String label, double startValue,
            LXParameter endValue, LXParameter periodMs) {
        this(label, new FixedParameter(startValue), endValue, periodMs);
    }

    public FunctionalModulator(String label, LXParameter startValue,
            LXParameter endValue, LXParameter periodMs) {
        super(label, startValue, endValue, periodMs);
    }

    @Override
    protected double computeNormalizedValue(double deltaMs, double basis) {
        double computed = this.compute(basis);
        if ((computed < 0) || (computed > 1)) {
            throw new IllegalStateException(getClass().getName()
                    + ".compute() must return a value between 0-1, returned " + computed
                    + " for argument " + basis);
        }
        return computed;
    }

    /**
     * Subclasses determine the basis based on a normalized value from 0 to 1.
     *
     * @param normalizedValue A normalize value from 0 to 1
     */
    @Override
    protected double computeNormalizedBasis(double basis, double normalizedValue) {
        double inverted = this.invert(basis, normalizedValue);
        if ((inverted < 0) || (inverted > 1)) {
            throw new IllegalStateException(getClass().getName()
                    + ".invert() must return a value between 0-1, returned " + inverted
                    + " for argument " + basis);
        }
        return inverted;
    }

    /**
     * Subclasses override this method to compute the value of the function. Basis
     * is a value from 0-1, the result must be a value from 0-1.
     *
     * @param basis Basis of modulator
     * @return Computed value for given basis
     */
    public abstract double compute(double basis);

    /**
     * Subclasses optionally override this method to support inversion of the
     * value to a basis. This is not well-defined for all functions. If it is not
     * implemented, an UnsupportedOperationException may be thrown at runtime on
     * invocations to methods that would directly change the value or bounds of
     * the function.
     *
     * @param basis Previous basis, from 0-1
     * @param value New value from 0-1
     * @return New basis, from 0-1
     */
    public double invert(double basis, double value) {
        throw new UnsupportedOperationException(
                this.getClass().getName()
                        + " does not implement invert(), may not directly change range or value.");
    }
}
