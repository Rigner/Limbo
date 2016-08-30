package net.rigner.limbo.util;

import java.util.UUID;

/**
 * Created by Rigner on 30/08/16 for project Limbo.
 * All rights reserved.
 */
public class UUIDConverter
{
    private UUIDConverter()
    {
    }

    public static UUID fromString(final String input)
    {
        return UUID.fromString(input.replaceFirst("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5"));
    }
}
