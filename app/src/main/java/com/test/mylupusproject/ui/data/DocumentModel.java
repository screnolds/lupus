package com.test.mylupusproject.ui.data;

import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.DocumentId;

public class DocumentModel extends ViewModel {

    private String docId;
    private String childrenType;
    private String path;

    public DocumentModel() {}

    public DocumentModel(String docId, String childrenType, String path) {
        this.docId = docId;
        this.childrenType = childrenType;
        this.path = path;
    }

    @DocumentId
    public String getDocId() {
        return this.docId;
    }

    @DocumentId
    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getChildrenType() {
        return this.childrenType;
    }

    public void setChildrenType(String childrenType) {
        this.childrenType = childrenType;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}