package alvin.example.com.tabtracker;

import android.provider.BaseColumns;

/**
 * Created by Alvin on 6/3/2016.
 */
public class TableData {

    public TableData() {}

    public static abstract class TableInfo implements BaseColumns {

        public static final String NAME = "name";
        public static final String AMOUNT = "amount";
        public static final String DATE = "date";
        public static final String REASON = "reason";
        public static final String POST_NUM = "post_num";
        public static final String TYPE = "post_type";
        public static final String LOAN = "loan";
        public static final String TABS_TABLE = "tabs_table";
        public static final String DATABASE_NAME = "tabs_data1";
    }
}
