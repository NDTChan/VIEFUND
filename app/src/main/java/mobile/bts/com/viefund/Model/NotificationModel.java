package mobile.bts.com.viefund.Model;


/**
 * BT Company
 * Created by Administrator on 3/30/2018.
 */

public class NotificationModel {
    public String Id,DateSend,SenderID,From,Subject,Content;

    public NotificationModel() {
    }

    public NotificationModel(String id, String dateSend, String senderID, String from, String subject,String content) {
        Id = id;
        DateSend = dateSend;
        SenderID = senderID;
        From = from;
        Subject = subject;
        Content = content;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getDateSend() {
        return DateSend;
    }

    public void setDateSend(String dateSend) {
        DateSend = dateSend;
    }

    public String getSenderID() {
        return SenderID;
    }

    public void setSenderID(String senderID) {
        SenderID = senderID;
    }

    public String getFrom() {
        return From;
    }

    public void setFrom(String from) {
        From = from;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}
