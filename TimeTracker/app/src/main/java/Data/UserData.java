package Data;

import java.util.ArrayList;

/**
 * Created by Klemen on 13. 03. 2017.
 */
public  class UserData {
    ArrayList<DownloadSpreadsheetData> downloadSpreadsheetDataList;
    ArrayList<Project> projectList;
    UploadSpreadsheetData uploadSpreadsheetData;

    public UserData(){
        uploadSpreadsheetData = new UploadSpreadsheetData();
        downloadSpreadsheetDataList = new ArrayList<DownloadSpreadsheetData>();
    }

    public void addDownloadRepository(DownloadSpreadsheetData downloadSpreadsheetData){
        this.downloadSpreadsheetDataList.add(downloadSpreadsheetData);
    }
    public void addUploadRepository(UploadSpreadsheetData data){
        this.uploadSpreadsheetData = data;
    }

    public UploadSpreadsheetData getUploadSpreadsheetData(){
        return this.uploadSpreadsheetData;
    }

    public void addProject(Project project){
        projectList.add(project);
    }
    public ArrayList<DownloadSpreadsheetData> getDownloadSpreadsheetDataList(){
        return  downloadSpreadsheetDataList;
    }
}
