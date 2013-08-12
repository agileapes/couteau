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

package com.agileapes.couteau.lang.compiler;

import com.agileapes.couteau.lang.support.Language;

/**
 * The SimpleSourceCompiler is an extension to {@link SimpleCompiler} that is designed to
 * separate and distinguish the two essential types of compilers:
 *
 * <ol>
 *     <li>Those that compile a code from one language and turn it into code of
 *     another, target language; e.g. Java-to-Ruby converters.</li>
 *     <li>Those that read source code from one language and turn it into usable
 *     target code of the same essence, in the same language; e.g. Java source
 *     to Java binary compilers.</li>
 * </ol>
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (5/21/13, 2:20 PM)
 */
public interface SimpleSourceCompiler<L extends Language> extends SimpleCompiler<L, L> {
}