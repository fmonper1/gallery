package uk.ac.shef.oak.com4510.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import java.util.List;

import uk.ac.shef.oak.com4510.database.PhotoData;
import uk.ac.shef.oak.com4510.pojo.FormData;
import uk.ac.shef.oak.com4510.repositories.PhotoRepository;

public class SearchViewModel extends AndroidViewModel {
    private FormData formData;
    private PhotoRepository mRepository;
    private LiveData<List<PhotoData>> allImages;


    public SearchViewModel(Application application) {
        super(application);
        mRepository = new PhotoRepository(application);
    }

    /**
     * getter for the live data
     * @return
     */
    public FormData getFormData() {
        if (formData == null) {
            formData = new FormData();
        }
        return formData;
    }

    public void submitFormData() {
        Log.d("formTitle", formData.getTitle());
        Log.d("formDescription", formData.getDescription());
        this.formData = formData;
    }

    /**
     * getter for the live data
     * @return - photoDataLiveData
     */
    public LiveData<List<PhotoData>> getAllImages() {
        if (allImages == null) {
            allImages = new MutableLiveData<List<PhotoData>>();
        }
        Log.d("formTitle viewmodel", formData.getTitle());

        if (formData.getTitle().equals("")) {
            // Search by description if title is empty
            allImages = mRepository.findImagesByDescription("%"+formData.getDescription()+"%");
        } else if (formData.getDescription().equals("")) {
            // Search by title if description is empty
            allImages = mRepository.findImagesByTitle("%"+formData.getTitle()+"%");
        } else {
            // Search by title and description
            allImages = mRepository.findImagesByTitleAndDescription("%"+formData.getTitle()+"%", "%"+formData.getDescription()+"%");
        }
        return allImages;
    }


    public boolean isDataNull() {
        if (formData.getTitle().equals("") && formData.getDescription().equals("")) {
            return true;
        } else {
            return false;
        }
    }
}
