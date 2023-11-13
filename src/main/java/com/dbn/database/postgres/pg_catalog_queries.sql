-- primary key constraints
select
    scm.NSPNAME as SCHEMA_NAME,
    tbl.RELNAME as TABLE_NAME,
    idx.RELNAME as INDEX_NAME,
    col.ATTNAME as COLUMN_NAME,
    def.ADSRC as DEFAULT_VALUE
from PG_CLASS tbl
     join PG_NAMESPACE scm on scm.OID = tbl.RELNAMESPACE
     join PG_ATTRIBUTE col on col.ATTRELID = tbl.OID
     join PG_INDEX ix on tbl.OID = ix.INDRELID and col.ATTNUM = ANY (ix.INDKEY)
     join PG_CLASS idx on idx.OID = ix.INDEXRELID
     left join PG_ATTRDEF def on def.ADRELID = tbl.OID and def.ADNUM = col.ATTNUM
where tbl.RELKIND =   'r'
order by
    scm.NSPNAME,
    tbl.RELNAME,
    idx.RELNAME,
    col.ATTNUM;