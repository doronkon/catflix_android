package com.example.catflix_android.Entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(
        tableName = "movies"
//        foreignKeys = @ForeignKey(
//                entity = Category.class,
//                parentColumns = "_id",
//                childColumns = "category",
//                onDelete = ForeignKey.CASCADE
//        ),
//        indices = {@Index(value = "category")}
)
public class Movie implements Serializable {
    @PrimaryKey
    @NonNull
    @SerializedName("_id")
    private String _id;
    private String pathToMovie;
    private String name;
    private String category;
    private String published;
    private String director;
    private String actors;
    private String thumbnail;
    private String length;
    private String description;
    private boolean catflixOriginal;
    private String minimalAge;

    public Movie(String _id,String pathToMovie, String minimalAge, boolean catflixOriginal, String description, String thumbnail, String length, String actors, String director, String category, String published, String name) {
        this._id = _id;
        this.pathToMovie = pathToMovie;
        this.minimalAge = minimalAge;
        this.catflixOriginal = catflixOriginal;
        this.description = description;
        this.thumbnail = thumbnail;
        this.length = length;
        this.actors = actors;
        this.director = director;
        this.category = category;
        this.published = published;
        this.name = name;
    }
    public String get_id(){
        return this._id;
    }


    public String getPathToMovie() {
        return pathToMovie;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getPublished() {
        return published;
    }

    public String getDirector() {
        return director;
    }

    public String getActors() {
        return actors;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getDescription() {
        return description;
    }

    public String getLength() {
        return length;
    }

    public boolean isCatflixOriginal() {
        return catflixOriginal;
    }

    public String getMinimalAge() {
        return minimalAge;
    }

    public void setPathToMovie(String pathToMovie) {
        this.pathToMovie = pathToMovie;
    }

    public void setMinimalAge(String minimalAge) {
        this.minimalAge = minimalAge;
    }

    public void setCatflixOriginal(boolean catflixOriginal) {
        this.catflixOriginal = catflixOriginal;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "pathToMovie='" + pathToMovie + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", published='" + published + '\'' +
                ", director='" + director + '\'' +
                ", actors='" + actors + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", length='" + length + '\'' +
                ", description='" + description + '\'' +
                ", catflixOriginal=" + catflixOriginal +
                ", minimalAge='" + minimalAge + '\'' +
                '}';
    }
}
