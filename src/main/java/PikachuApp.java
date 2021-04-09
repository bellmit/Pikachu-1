import com.pikachu.common.annotations.IColumn;
import com.pikachu.common.annotations.ITable;
import com.pikachu.common.collection.Where;
import com.pikachu.common.database.core.Database;
import com.pikachu.framework.database.DaoManager;
import com.pikachu.framework.database.IDao;
import com.pikachu.framework.database.core.DatabaseConfig;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Date;
import java.util.StringJoiner;

public class PikachuApp {

    public static void main(String[] args) {
        DatabaseConfig config = new DatabaseConfig();
        config.setName("AD");
        config.setDriver("com.mysql.cj.jdbc.Driver");
        config.setUrl("jdbc:mysql://localhost:3306/ad?characterEncoding=utf8&useSSL=false&serverTimezone=UTC&rewriteBatchedStatements=true");
        config.setUser("root");
        config.setPassword("123456");
        DaoManager daoManager = null;
        try {
            daoManager = new DaoManager(config);
            IDao<Pikachu> pikachuDao = daoManager.getDao(Pikachu.class);

            Pikachu p = pikachuDao.add(new Pikachu());
            Pikachu[] list = pikachuDao.getList(null, null);

            for (Pikachu pikachu : list) {
                System.out.println(pikachu);
            }


        } catch (Exception e) {
            e.printStackTrace();
            if (daoManager != null) {
                daoManager.stop();
            }
        }
    }

    @ITable(doc = "测试表", cache = true, history = true)
    public static class Pikachu implements Serializable {

        @IColumn(doc = "主键", pk = true)
        private String id;

        private String name = "AD";

        private int age = 1;

        private boolean sex = true;

        private long salary_bigint = 123456789;

        private BigDecimal salary_decimal = new BigDecimal(987654321);

        private double salary_double = 123.456;

        private float salary_float = 456.1F;

        private LocalTime birthday_time = LocalTime.now();

        private LocalDate birthday_date = LocalDate.now();

        private LocalDateTime birthday_datetime = LocalDateTime.now();

        private Date birthday_timestamp = new Date();

        private byte[] blob_blob = new byte[]{1, 2, 3};

        private String clob_clob = "这是一个clob文本，数据库是text";

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public boolean isSex() {
            return sex;
        }

        public void setSex(boolean sex) {
            this.sex = sex;
        }

        public long getSalary_bigint() {
            return salary_bigint;
        }

        public void setSalary_bigint(long salary_bigint) {
            this.salary_bigint = salary_bigint;
        }

        public BigDecimal getSalary_decimal() {
            return salary_decimal;
        }

        public void setSalary_decimal(BigDecimal salary_decimal) {
            this.salary_decimal = salary_decimal;
        }

        public double getSalary_double() {
            return salary_double;
        }

        public void setSalary_double(double salary_double) {
            this.salary_double = salary_double;
        }

        public float getSalary_float() {
            return salary_float;
        }

        public void setSalary_float(float salary_float) {
            this.salary_float = salary_float;
        }

        public LocalTime getBirthday_time() {
            return birthday_time;
        }

        public void setBirthday_time(LocalTime birthday_time) {
            this.birthday_time = birthday_time;
        }

        public LocalDate getBirthday_date() {
            return birthday_date;
        }

        public void setBirthday_date(LocalDate birthday_date) {
            this.birthday_date = birthday_date;
        }

        public LocalDateTime getBirthday_datetime() {
            return birthday_datetime;
        }

        public void setBirthday_datetime(LocalDateTime birthday_datetime) {
            this.birthday_datetime = birthday_datetime;
        }

        public Date getBirthday_timestamp() {
            return birthday_timestamp;
        }

        public void setBirthday_timestamp(Date birthday_timestamp) {
            this.birthday_timestamp = birthday_timestamp;
        }

        public byte[] getBlob_blob() {
            return blob_blob;
        }

        public void setBlob_blob(byte[] blob_blob) {
            this.blob_blob = blob_blob;
        }

        public String getClob_clob() {
            return clob_clob;
        }

        public void setClob_clob(String clob_clob) {
            this.clob_clob = clob_clob;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Pikachu.class.getSimpleName() + "[", "]")
                    .add("id='" + id + "'")
                    .add("name='" + name + "'")
                    .add("age=" + age)
                    .add("sex=" + sex)
                    .add("salary_bigint=" + salary_bigint)
                    .add("salary_decimal=" + salary_decimal)
                    .add("salary_double=" + salary_double)
                    .add("salary_float=" + salary_float)
                    .add("birthday_time=" + birthday_time)
                    .add("birthday_date=" + birthday_date)
                    .add("birthday_datetime=" + birthday_datetime)
                    .add("birthday_timestamp=" + birthday_timestamp)
                    .add("blob_blob=" + Arrays.toString(blob_blob))
                    .add("clob_clob='" + clob_clob + "'")
                    .toString();
        }

    }

}
