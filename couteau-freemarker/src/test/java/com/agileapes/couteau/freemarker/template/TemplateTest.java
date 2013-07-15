package com.agileapes.couteau.freemarker.template;

import com.agileapes.couteau.freemarker.test.model.Artist;
import com.agileapes.couteau.freemarker.test.model.Song;
import com.agileaps.couteau.freemarker.utils.FreemarkerUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (7/15/13, 8:27 PM)
 */
public class TemplateTest {

    @Test
    public void testSimpleTemplate() throws Exception {
        final Artist artist = new Artist();
        artist.setName("Sting");
        artist.setGenre("Pop");
        final Song song = new Song();
        song.setArtist(artist);
        song.setTitle("Shape of My Heart");
        song.setYear(1998);
        song.setLyrics("He deals the cards as a meditation,\nand those he plays never suspect\n" +
                "He doesn't play for the money he wins\nHe don't play for respect...");
        final String content = FreemarkerUtils.writeString(song);
        Assert.assertNotNull(content);
    }
}
