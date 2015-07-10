package com.blockhead.bhmusic.objects;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.graphics.Palette;
import android.util.Log;

import com.blockhead.bhmusic.activities.MainActivity;

import java.util.ArrayList;

/**
 * Created by Gus on 2/26/2015.
 */
public class Artist {

    public ArrayList<Song> tracks = new ArrayList<Song>();
    public ArrayList<Album> albums = new ArrayList<Album>();
    private String name, summaryHTML, imagePath;
    private int accentColor = Color.WHITE;
    private int randomColor;
    private boolean accentColorSet = false;

    public Artist(String artistName)
    {
        name = artistName;
        randomColor = MainActivity.randomColor();
    }

    public int getRandomColor(){ return randomColor; }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(String pathName,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(pathName, options);
    }

    public String getName() {
        return name;
    }

    public ArrayList<Song> getTracks(){ return tracks; }

    public ArrayList<Album> getAlbums() {
        return albums;
    }

    public int getAccentColor()
    {

        if (imagePath != null && !accentColorSet)
        {
            setAccentColor();
        }

        return accentColor;
    }

    public boolean addDummyAlbum(String artist)
    {
        Album dummy = new Album("","",artist);
        dummy.setArtistObj(this);
        Song dummySong = new Song(0,"dummy",artist, "", -1, "0");
        dummySong.setAlbumObj(dummy);
        dummy.addSong(dummySong);
        albums.add(dummy);
        if(albums.size() == 1)
            return true;
        else
            return false;
    }

    public void addAlbum(Album newAlbum) {
        try {
            albums.add(newAlbum);
            tracks.addAll(newAlbum.getTracks());
        } catch (NullPointerException e) {
            Log.d("BHCA", "EXCEPTION CAUGHT:" + e.getMessage());
        }
    }

    public void setAccentColor()
    {
        if(!accentColorSet) {
            Bitmap image = decodeSampledBitmapFromResource(imagePath, 200, 200);
            if (image != null) {
                Palette palette = Palette.from(image).generate();

                accentColor = palette.getVibrantColor(Color.WHITE);
                if (accentColor == Color.WHITE)
                    accentColor = palette.getMutedColor(Color.WHITE);
            }

            accentColorSet = true;
        }
    }

    public void setImagePath(String path){ imagePath = path; setAccentColor(); }

    public String getImagePath()
    {
        if(imagePath == null)
            return null;
        else if(imagePath.isEmpty())
            return null;
        else
            return "file:///" + imagePath;
    }

    public void setSummary(String sum)
    {
        summaryHTML = sum;
    }

    public String getSummaryHTML()
    {
        return summaryHTML;
    }


}
