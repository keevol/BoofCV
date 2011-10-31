/*
 * Copyright (c) 2011, Peter Abeles. All Rights Reserved.
 *
 * This file is part of BoofCV (http://www.boofcv.org).
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

package boofcv.benchmark.opencv;

import boofcv.abst.feature.detect.extract.GeneralFeatureDetector;
import boofcv.alg.filter.blur.GBlurImageOps;
import boofcv.alg.filter.derivative.GImageDerivativeOps;
import boofcv.core.image.ConvertBufferedImage;
import boofcv.core.image.GeneralizedImageOps;
import boofcv.core.image.border.BorderType;
import boofcv.factory.feature.detect.interest.FactoryCornerDetector;
import boofcv.io.image.UtilImageIO;
import boofcv.misc.PerformerBase;
import boofcv.misc.ProfileOperation;
import boofcv.struct.image.ImageBase;
import boofcv.struct.image.ImageFloat32;

import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * @author Peter Abeles
 */
// todo general convolution
// todo canny edge
public class BenchmarkGaussianBlur<T extends ImageBase, D extends ImageBase> {

	final Class<T> imageType;
	final Class<D> derivType;
	final T input;

	final static String imageName = "data/standard/barbara.png";
	final int radius = 2;
	final long TEST_TIME = 1000;
	final Random rand = new Random(234234);

	public BenchmarkGaussianBlur( BufferedImage image , Class<T> imageType , Class<D> derivType ) {
		this.imageType = imageType;
		this.derivType = derivType;
		this.input = ConvertBufferedImage.convertFrom(image,null,imageType);
	}

	public class Gaussian extends PerformerBase
	{
		T output = (T)input._createNew(input.width,input.height);
		T storage = (T)input._createNew(input.width,input.height);

		@Override
		public void process() {
			GBlurImageOps.gaussian(input,output,-1,radius,storage);
		}
	}

	public class Sobel extends PerformerBase
	{
		D derivX = GeneralizedImageOps.createImage(derivType,input.width,input.height);
		D derivY = GeneralizedImageOps.createImage(derivType,input.width,input.height);

		@Override
		public void process() {
			GImageDerivativeOps.sobel(input, derivX,derivY, BorderType.EXTENDED);
		}
	}

	public class Harris extends PerformerBase
	{
		D derivX = GeneralizedImageOps.createImage(derivType,input.width,input.height);
		D derivY = GeneralizedImageOps.createImage(derivType,input.width,input.height);
		GeneralFeatureDetector<T,D> detector;

		public Harris() {
			GImageDerivativeOps.sobel(input, derivX,derivY, BorderType.EXTENDED);
			detector = FactoryCornerDetector.createHarris(radius,1,-1,derivType);
		}

		@Override
		public void process() {
			detector.process(input,derivX,derivY,null,null,null);
		}
	}


	public void performTest() {
		System.out.println("=========  Profile Description width = "+input.width+" height = "+input.height);
		System.out.println();

//		ProfileOperation.printOpsPerSec(new Gaussian(), TEST_TIME);
//		ProfileOperation.printOpsPerSec(new Sobel(), TEST_TIME);
		ProfileOperation.printOpsPerSec(new Harris(), TEST_TIME);

		System.out.println();
	}

	public static void main( String args[] ) {
		BufferedImage image = UtilImageIO.loadImage(imageName);

		BenchmarkGaussianBlur<ImageFloat32,ImageFloat32> test =
				new BenchmarkGaussianBlur<ImageFloat32,ImageFloat32>(image, ImageFloat32.class,ImageFloat32.class);

		test.performTest();
	}
}
