package gstores.merchandiser_beta.localdata;

public interface ICommandBase {

    static final String NAME = "merchanrt_daily.db";
    static final String[] SCRIPT_VERSION_1 = {
            "CREATE TABLE product_line_master ( product_line_id INTEGER PRIMARY KEY, description TEXT )",
            "CREATE TABLE brand_master ( brand_id INTEGER PRIMARY KEY, description TEXT, product_line_id INTEGER )",
            "CREATE TABLE group_master ( group_id INTEGER PRIMARY KEY, group_name TEXT )",
            "CREATE TABLE model_master ( model_id INTEGER PRIMARY KEY, description TEXT )",
            "CREATE TABLE user_master ( is_login INTEGER DEFAULT ( 0 ), email TEXT, second_name TEXT, first_name TEXT, user_id INTEGER PRIMARY KEY, user_name TEXT, password TEXT, group_id INTEGER REFERENCES group_master ( group_id )  )",
            "CREATE TABLE notification_master ( create_user_id INTEGER NOT NULL REFERENCES user_master ( user_id ) , notification_id INTEGER PRIMARY KEY, title TEXT, description TEXT, time TEXT, is_view INTEGER DEFAULT ( 0 ), CHECK ( is_view = 0 OR is_view = 1 ) )",
            "CREATE TABLE catogery_master ( category_id INTEGER PRIMARY KEY, description TEXT )",
            "CREATE TABLE item_master ( inventory_item_id INTEGER PRIMARY KEY, item_code TEXT, product_line_id INTEGER , brand_id INTEGER NOT NULL REFERENCES brand_master ( brand_id ) , description TEXT, model_id INTEGER NOT NULL REFERENCES model_master ( model_id ) , category_id INTEGER REFERENCES catogery_master ( category_id ))",
            "CREATE TABLE user_item_config ( user_id INTEGER REFERENCES user_master ( user_id ), inventory_item_id INTEGER REFERENCES item_master ( inventory_item_id ) )",
            "CREATE TABLE daily_updates ( is_posted INTEGER ,last_update_time TEXT ,location TEXT ,create_time TEXT ,user_id INTEGER REFERENCES user_master(user_id) ,daily_update_id INTEGER PRIMARY KEY ,product_line_id INTEGER REFERENCES product_line_master(product_line_id) ,brand_id INTEGER REFERENCES brand_master(brand_id) ,model_id INTEGER REFERENCES model_master(model_id) ,quantity INTEGER ,posted_daily_update_id INTEGER ,inventory_item_id INTEGER REFERENCES item_master(inventory_item_id) ,UNIQUE(posted_daily_update_id ASC) ,UNIQUE ( create_time ASC ,user_id ,model_id ASC ) ,CHECK ( is_posted = 0 OR is_posted = 1 ) )",
            //"INSERT INTO [group_master] ([group_id], [group_name]) VALUES (1, 'app')",
            "INSERT INTO [group_master] ([group_id], [group_name]) VALUES (2,'Merchant')"//,
            //"INSERT INTO [user_master] ([is_login], [email], [second_name], [first_name], [user_id], [user_name], [password], [group_id]) VALUES (0, 'srkrishnan@gstores.ae', 'Radhakrishnan', 'Sujith', 1, 'srkrishnan', 'pass', 1)",
            //"INSERT INTO [user_master] ([is_login], [email], [second_name], [first_name], [user_id], [user_name], [password], [group_id]) VALUES (0, 'test@gstores.ae', 'Gstores', 'Test', 2, 'test', 'pass', 1)"
    };
    static final String[] SCRIPT_VERSION_2 = {
            "ALTER TABLE daily_updates ADD COLUMN value INTEGER DEFAULT 0",
            "CREATE TABLE TEST (name TEXT)",
            "CREATE TABLE daily_updates_temp (\n" +
                    "  is_posted integer,\n" +
                    "  last_update_time text,\n" +
                    "  location text,\n" +
                    "  create_time text,\n" +
                    "  user_id integer,\n" +
                    "  daily_update_id integer,\n" +
                    "  product_line_id integer,\n" +
                    "  brand_id integer,\n" +
                    "  model_id integer,\n" +
                    "  quantity integer,\n" +
                    "  posted_daily_update_id integer,\n" +
                    "  inventory_item_id integer,\n" +
                    "  value INTEGER DEFAULT 0 \n"+
                    ")"};
    static final String[] SCRIPT_VERSION_3 = {
            "INSERT INTO daily_updates_temp (is_posted,\n" +
                    "last_update_time,\n" +
                    "location,\n" +
                    "create_time,\n" +
                    "user_id,\n" +
                    "daily_update_id,\n" +
                    "product_line_id,\n" +
                    "brand_id,\n" +
                    "model_id,\n" +
                    "quantity,\n" +
                    "posted_daily_update_id,\n" +
                    "inventory_item_id" +
                    "value)\n" +
                    "  SELECT\n" +
                    "    is_posted,\n" +
                    "    last_update_time,\n" +
                    "    location,\n" +
                    "    create_time,\n" +
                    "    user_id,\n" +
                    "    daily_update_id,\n" +
                    "    product_line_id,\n" +
                    "    brand_id,\n" +
                    "    model_id,\n" +
                    "    quantity,\n" +
                    "    posted_daily_update_id,\n" +
                    "    inventory_item_id,\n" +
                    "    value \n" +
                    "  FROM daily_updates",
            "DROP TABLE daily_updates",
            "CREATE TABLE daily_updates (\n" +
                    "  is_posted integer,\n" +
                    "  last_update_time text,\n" +
                    "  location text,\n" +
                    "  create_time text,\n" +
                    "  user_id integer REFERENCES user_master (user_id),\n" +
                    "  daily_update_id integer PRIMARY KEY,\n" +
                    "  product_line_id integer REFERENCES product_line_master (product_line_id),\n" +
                    "  brand_id integer REFERENCES brand_master (brand_id),\n" +
                    "  model_id integer REFERENCES model_master (model_id),\n" +
                    "  quantity integer,\n" +
                    "  posted_daily_update_id integer,\n" +
                    "  inventory_item_id integer REFERENCES item_master (inventory_item_id),\n" +
                    "  value INTEGER DEFAULT 0, \n" +
                    "  UNIQUE (posted_daily_update_id ASC),\n" +
                    "  CHECK (is_posted = 0 OR is_posted = 1)\n" +
                    ")"
    };

    static final String[] SCRIPT_VERSION_4 = {
            "CREATE TABLE merchant_checkin_m(\n"+
                    " id integer,\n"+
                    " user_id integer,\n"+
                    " checkin_time TEXT,\n"+
                    " latitude TEXT,\n"+
                    " latitude TEXT,\n"+
                    ")"
    };

    static final String[] SCRIPT_VERSION_5 = {
            "ALTER TABLE daily_updates\n"+
                    "ADD COLUMN customer_name TEXT default null",
            "ALTER TABLE daily_updates\n"+
                    "ADD COLUMN customer_phone TEXT default null",
            "ALTER TABLE daily_updates\n"+
                    "ADD COLUMN emirate TEXT default null",
            "ALTER TABLE daily_updates\n"+
                    "ADD COLUMN po TEXT default null",
    };
    static final String USER_LOGIN_WHERE = "user_name=? and password = ?";
    static final String GET_CURRENT_USER = "SELECT user_id, user_name, password, group_id,first_name,second_name FROM user_master WHERE is_login = ?";
    static final String GET_GROUP = "SELECT group_name FROM group_master WHERE group_id = ?";
    static final String GET_MODEL_BRAND = "select brand_id from item_master where model_id = ?";
    static final String GET_MODEL_PRODUCT_LINE = "select product_line_id from item_master where model_id = ?";
    static final String GET_PRODUCT_LINE = "SELECT product_line_id,description FROM product_line_master";
    static final String GET_PRODUCT_LINE_BRAND = "SELECT brand_id, description FROM brand_master where product_line_id = ?";
    static final String GET_BRAND = "select distinct brand_master.brand_id,brand_master.description from brand_master inner join item_master on brand_master.brand_id = item_master.brand_id";
    static final String GET_BRAND_MODEL = "select item_master.model_id ,model_master.description from item_master INNER JOIN model_master ON item_master.model_id = model_master.model_id where item_master.brand_id = ?";
    static final String GET_CATEGORY_MODEL = "select item_master.model_id ,model_master.description from item_master INNER JOIN model_master ON item_master.model_id = model_master.model_id where item_master.category_id = ?";
    static final String GET_BRAND_CATEGORY_MODEL = "select item_master.model_id ,model_master.description from item_master INNER JOIN model_master ON item_master.model_id = model_master.model_id where item_master.brand_id = ? and item_master.category_id = ?";
    static final String GET_MODEL = "SELECT model_id,description FROM model_master where model_id in (select model_id from item_master)";
    static final String GET_COMMENTS = "select user_id, title, comment from daily_updates";
    static final String GET_USER = "SELECT user_id, user_name, password, group_id,first_name,second_name FROM user_master user_id = ?";
    static final String GET_DAILY_REPORT = "select daily_update_id,model_id,title,comment,quantity from daily_updates";
    static final String GET_MODEL_FOR_ID = "SELECT * FROM model_master WHERE model_id = ?";

    static final String INSERT_BRAND = "insert into brand_master (brand_id,description)values(?,?)";
    static final String INSERT_CATEGORY = "INSERT INTO catogery_master (category_id,description) VALUES (?,?)";
    static final String INSERT_MODEL = "INSERT INTO model_master (model_id,description) VALUES (?,?)";
    static final String INSERT_ITEM = "INSERT INTO item_master(inventory_item_id,brand_id,catogery_id,model_id,item_code,description)VALUES(?,?,?,?,?,?)";
    static final String GET_CATEGORY_LIST = "select category_id,description from catogery_master";
    static final String GET_ITEM_MASTER = "select inventory_item_id, brand_id,category_id ,item_code, model_id, description from item_master where brand_id like ? and category_id like ? and model_id like ?";
    static final String GET_BRAND_WITH_ID = "select brand_id, description from brand_master where brand_id = ?";
    static final String GET_CATEGORY_WITH_ID = "SELECT category_id,description FROM catogery_master WHERE category_id = ?";
    static final String GET_UNPOSTED_DAILY_SALES = "select daily_update_id ,user_id,create_time,inventory_item_id,model_id,quantity,value ,is_posted from daily_updates where is_posted = 0";
    static final String GET_DAILY_SALES = "select daily_update_id ,user_id,create_time,inventory_item_id,model_id,quantity,value, is_posted from daily_updates WHERE user_id = ? order by daily_update_id desc";
    static final String SELECT_LAST_NOTIFICATION = "select max(notification_id) from notification_master";
    static final String GET_ALL_NOTIFICATIONS = "select notification_id, title, description, time, is_view from notification_master order by notification_id desc";
    static final String GET_NOTIFICATION = "select notification_id, title, description, time from notification_master where notification_id = ?";
    static final String GET_MODEL_INVENTORY_ID = "select max(item_master.inventory_item_id) as inventory_item_id from item_master  where model_id = ?";
    static final String GET_ITEM_BRAND = "select brand_master.brand_id,brand_master.description from item_master INNER JOIN brand_master on item_master.brand_id = brand_master.brand_id where inventory_item_id = ?";
    static final String GET_MODEL_NAME = "select description from model_master where model_id = ?";
}