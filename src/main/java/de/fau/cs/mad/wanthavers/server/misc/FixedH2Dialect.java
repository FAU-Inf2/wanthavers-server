package de.fau.cs.mad.wanthavers.server.misc;

import org.hibernate.dialect.H2Dialect;

public class FixedH2Dialect extends H2Dialect
{
    public FixedH2Dialect()
    {
        registerColumnType( java.sql.Types.FLOAT, "double" );
    }
}
