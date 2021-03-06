/*
 * Copyright (c) 2011-2020, Peter Abeles. All Rights Reserved.
 *
 * This file is part of BoofCV (http://boofcv.org).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package boofcv.abst.feature.detect.interest;

import boofcv.abst.feature.detect.extract.ConfigExtract;
import boofcv.struct.Configuration;

/**
 * Configuration for {@link boofcv.alg.feature.detect.interest.FastHessianFeatureDetector} plus feature extractor.
 *
 * @author Peter Abeles
 */
public class ConfigFastHessian implements Configuration {

	/**
	 * Configuration for non-maximum thresholding
	 */
	public ConfigExtract extract = new ConfigExtract(2,1,0,true);

	/**
	 * Maximum number of features that non-maximum suppression (NMS) can return from each scale. Features with higher
	 * intensity scores are given preference.  If &le; 0 NMS will return all features it finds.
	 */
	public int maxFeaturesPerScale = -1;

	/**
	 * How often pixels are sampled in the first octave.  Typically 1 or 2.
	 */
	public int initialSampleStep = 1;

	/**
	 * Typically 9.
	 */
	public int initialSize = 9;

	/**
	 * Typically 4.
	 */
	public int numberScalesPerOctave = 4;

	/**
	 * Typically 4.
	 */
	public int numberOfOctaves = 4;

	/**
	 * Increment between kernel sizes as it goes up in scale.  In some data sets, increasing this value beyound
	 * the default value results in an improvement in stability.  Default 6
	 */
	public int scaleStepSize = 6;

	public ConfigFastHessian(float detectThreshold,
							 int extractRadius,
							 int maxFeaturesPerScale,
							 int initialSampleStep,
							 int initialSize,
							 int numberScalesPerOctave,
							 int numberOfOctaves) {
		this.extract.threshold = detectThreshold;
		this.extract.radius = extractRadius;
		this.maxFeaturesPerScale = maxFeaturesPerScale;
		this.initialSampleStep = initialSampleStep;
		this.initialSize = initialSize;
		this.numberScalesPerOctave = numberScalesPerOctave;
		this.numberOfOctaves = numberOfOctaves;
	}

	public ConfigFastHessian() {
	}

	@Override
	public void checkValidity() {
	}

	public void setTo( ConfigFastHessian src ) {
		this.extract.setTo(src.extract);
		this.maxFeaturesPerScale = src.maxFeaturesPerScale;
		this.initialSampleStep = src.initialSampleStep;
		this.initialSize = src.initialSize;
		this.numberScalesPerOctave = src.numberScalesPerOctave;
		this.numberOfOctaves = src.numberOfOctaves;
		this.scaleStepSize = src.scaleStepSize;
	}

	public ConfigFastHessian copy() {
		var dst = new ConfigFastHessian();
		dst.setTo(this);
		return dst;
	}
}
