package de.fau.cs.mad.wanthavers.server.misc;

import org.hibernate.dialect.HSQLDialect;

/**
 * 'Fixed' HSQL Dialect.
 *
 * PEARL: HSQL seems to have a problem with floats.  We remedy this here.
 * See http://stackoverflow.com/q/28480714/773113
 *
 * PEARL: this class must be public, not package-private, and it must have a
 * public constructor, otherwise hibernate won't be able to instantiate it.
 */
public class FixedHsqlDialect extends HSQLDialect
{
    public FixedHsqlDialect()
    {
        registerColumnType( java.sql.Types.FLOAT, "double" );
    }
}
