package com.hy0417sage.notes.funtion.in;

public interface IMemo {

    void saveMemo(long id, String title, String content);
    void editMemo(long id, String title, String content);
    void deleteMemo(long id);

}
