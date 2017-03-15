package Data;

import java.util.ArrayList;

/**
 * Created by Klemen on 13. 03. 2017.
 */
public  class DataAll {
    ArrayList<DownloadRepository> downloadRepositoryList;
    UploadRepository uploadRepository;

    public DataAll(){
        uploadRepository = new UploadRepository();
        downloadRepositoryList = new ArrayList<DownloadRepository>();
    }

    public void addDownloadRepository(DownloadRepository downloadRepository){
        this.downloadRepositoryList.add(downloadRepository);
    }
    public void addUploadRepository(UploadRepository data){
        this.uploadRepository = data;
    }

    public UploadRepository getUploadRepository(){
        return this.uploadRepository;
    }
}
