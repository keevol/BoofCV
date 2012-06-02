package boofcv.abst.sfm;

import boofcv.alg.sfm.StereoSimpleVo;
import boofcv.alg.tracker.AccessPointTracks;
import boofcv.struct.image.ImageSingleBand;
import georegression.struct.point.Point2D_F64;
import georegression.struct.se.Se3_F64;

import java.util.List;

/**
 * @author Peter Abeles
 */
public class WrapStereoSimpleVo<T extends ImageSingleBand>
		implements StereoVisualOdometry<T>, AccessPointTracks

{

	StereoSimpleVo<T> alg;

	Se3_F64 c2w = new Se3_F64();

	public WrapStereoSimpleVo(StereoSimpleVo<T> alg) {
		this.alg = alg;
	}

	@Override
	public boolean process(T leftImage, T rightImage) {
		return alg.process(leftImage,rightImage);
	}

	@Override
	public void reset() {
		//Todo implement
	}

	@Override
	public boolean isFatal() {
		return alg.hadFault();
	}

	@Override
	public Se3_F64 getCameraToWorld() {
		Se3_F64 w2c = alg.getWorldToCamera();
		w2c.invert(c2w);
		return c2w;
	}

	@Override
	public List<Point2D_F64> getAllTracks() {
		return (List)alg.getTracker().getActiveTracks();
	}

	@Override
	public List<Point2D_F64> getInlierTracks() {
		return null;
	}

	@Override
	public List<Point2D_F64> getNewTracks() {
		return (List)alg.getTracker().getTracker().getNewTracks();
	}
}
