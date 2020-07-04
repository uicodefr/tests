package test.uicode.mongoperf.entity;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "baseEntityLight")
public class BaseEntity {

    @Id
    private String id;

    private String name;

    private List<NameDocument> names;

    private Date date;

    private List<SubDocument> documentList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<NameDocument> getNames() {
        return names;
    }

    public void setNames(List<NameDocument> names) {
        this.names = names;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<SubDocument> getDocumentList() {
        return documentList;
    }

    public void setDocumentList(List<SubDocument> documentList) {
        this.documentList = documentList;
    }

}
