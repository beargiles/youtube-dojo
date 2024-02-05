/*
 * Copyright (c) 2023 Bear Giles <bgiles@coyotesong.com>.
 * All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.coyotesong.dojo.youtube.service;

import com.coyotesong.tabs.repo.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Optional;

/**
 * Spring MessageSource backed by a database
 */
@Service
public class DatabaseMessageSource implements MessageSource {
    private static final Logger LOG = LoggerFactory.getLogger(DatabaseMessageSource.class);

    private MessageRepository messageDao;

    @Autowired
    public DatabaseMessageSource(MessageRepository messageDao) {
        this.messageDao = messageDao;
    }

    /**
     * Try to resolve the message. Return default message if no message was found.
     *
     * @param code the message code to look up, e.g. 'calculator.noRateSet'. MessageSource users are encouraged to base message names on qualified class or package names, avoiding potential conflicts and ensuring maximum clarity.
     * @param args an array of arguments that will be filled in for params within the message (params look like "{0}", "{1,date}", "{2,time}" within a message), or null if none
     * @param defaultMessage a default message to return if the lookup fails
     * @param locale the locale in which to do the lookup
     * @return the resolved message if the lookup was successful, otherwise the default message passed as a parameter (which may be null)
     */
    @Override
    public String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
        Optional<String> message = messageDao.findOptionalForCodeAndLocale(code, locale);
        return message.orElse(defaultMessage);
    }

    /**
     * Try to resolve the message. Treat as an error if the message can't be found.
     *
     * @param code the message code to look up, e.g. 'calculator.noRateSet'. MessageSource users are encouraged to base message names on qualified class or package names, avoiding potential conflicts and ensuring maximum clarity.
     * @param args an array of arguments that will be filled in for params within the message (params look like "{0}", "{1,date}", "{2,time}" within a message), or null if none
     * @param locale the locale in which to do the lookup
     * @return the resolved message (never null)
     * @throws NoSuchMessageException if no corresponding message was found
     */
    @Override
    public String getMessage(String code, Object[] args, Locale locale) throws NoSuchMessageException {
        Optional<String> message = messageDao.findOptionalForCodeAndLocale(code, locale);
        if (message.isEmpty()) {
            throw new NoSuchMessageException(code, locale);
        }
        return message.get();
    }

    /**
     * Try to resolve the message using all the attributes contained within the MessageSourceResolvable argument that was passed in.
     *
     * @param resolvable the value object storing attributes required to resolve a message (may include a default message)
     * @param locale the locale in which to do the lookup
     * @return the resolved message (never null since even a MessageSourceResolvable-provided default message needs to be non-null)
     * @throws NoSuchMessageException if no corresponding message was found (and no default message was provided by the MessageSourceResolvable)
     */
    @Override
    public String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException {
        LOG.warn("unimplemented method");
        return null;
    }
} 
