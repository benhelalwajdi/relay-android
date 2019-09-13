package app.com.relay.model;

public class Review {

    private String id;
    private String rating;
    private String comment;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id='" + id + '\'' +
                ", rating='" + rating + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}
