package com.github.pokee.stick.response.writers;

import com.github.pokee.stick.Version;

public class ResponseWriterVersion1_1 extends ResponseWriterVersion1_0 {

    @Override
    protected Version getVersion() {
        return Version.VERSION_1_1;
    }

}
