/*
 * Copyright (c) 2013. AgileApes (http://www.agileapes.scom/), and
 * associated organization.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 */

package com.agileapes.couteau.freemarker.template;

import com.agileapes.couteau.freemarker.test.model.Artist;
import com.agileapes.couteau.freemarker.test.model.Song;
import com.agileapes.couteau.freemarker.utils.FreemarkerUtils;
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
