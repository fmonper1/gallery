package uk.ac.shef.oak.com4510.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.maps.model.LatLngBounds;

import java.util.List;

import uk.ac.shef.oak.com4510.database.PhotoData;
import uk.ac.shef.oak.com4510.pojo.FormData;
import uk.ac.shef.oak.com4510.repositories.PhotoRepository;


public class MapsViewModel extends AndroidViewModel {

    private PhotoRepository mRepository;
    private LiveData<List<PhotoData>> locationImages;

    public MapsViewModel(@NonNull Application application) {
        super(application);
        mRepository = new PhotoRepository(application);
    }

    /**
     * returns a list of live data images returned by the query in the repository
     * @return
     */
    public LiveData<List<PhotoData>> getGeoLocatedImages(){
        locationImages = mRepository.findGeoLocatedImages();

        return locationImages;
    }

    /**
     * takes the lat and long boundaries of the map and
     * returns all images produces from this query in the repository
     * @param latLngBounds
     * @return
     */
    public LiveData<List<PhotoData>> getImagesInsideBounds(LatLngBounds latLngBounds) {

        locationImages = mRepository.findImagesInsideBounds(latLngBounds);
        return locationImages;
    }
}
