package com.hy0417sage.notes.funtion.in;

import android.net.Uri;

import java.util.List;

public interface IImageLink {

    void inputLinkDialog(String linkUri);
    void addImageLink(List<Uri> linkUriList, String linkUri);

}
