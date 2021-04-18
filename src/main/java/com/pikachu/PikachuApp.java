package com.pikachu;

import com.pikachu.common.annotations.IColumn;
import com.pikachu.common.annotations.ITable;
import com.pikachu.common.collection.Where;
import com.pikachu.common.database.pool.core.PoolType;
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

/**
 * @Desc TODO
 * @Date 2021/4/9 20:46
 * @Author AD
 */
public class PikachuApp {

    public static void main(String[] args) {
        DatabaseConfig c = new DatabaseConfig();
        c.setName("Pikachu");

        c.setDriver("com.mysql.cj.jdbc.Driver");
        c.setUrl("jdbc:mysql://localhost:3306/study?serverTimezone=UTC&characterEncoding=utf8&userUnicode=true&userSSL=false");
        c.setUser("root");
        c.setPassword("123456");

        //c.setDriver("oracle.jdbc.driver.OracleDriver");
        //c.setUrl("jdbc:oracle:thin:@//172.26.1.168:1521/dccsdev");
        //c.setUser("DCCS");
        //c.setPassword("hqj__ZS3V1iHo--yGbi-");
        c.setPoolType(PoolType.HIKARI.toString());

        try {
            DaoManager daoManager = new DaoManager(c);
            IDao<Pikachu> dao = daoManager.getDao(Pikachu.class);
            Pikachu[] ps = dao.getList(null, null);
            for (Pikachu p : ps) {
                System.out.println(p);
            }
            Where w = new Where();
            w.setK("name");
            w.setO("=");
            w.setV("AD");
    
            Where where = new Where();
            where.setK("age");
            where.setO("=");
            where.setV("2");
            int delete = dao.delete(new Where[]{w, where});
            System.out.println(ps.length);
            System.out.println(delete);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ITable(doc = "测试", cache = true, history = true)
    public static class Pikachu implements Serializable {

        @IColumn(doc = "主键", pk = true)
        private String id;
        private String name = "AD";
        private int age = 1;
        private double salary = 1.234;
        private boolean sex = true;
        private long phone = 18606051513L;
        private LocalDateTime birthday = LocalDateTime.now();
        private Date now = new Date();
        private byte[] bytes = new byte[]{1, 2, 3};
        private String clobs = "clobs";
        private BigDecimal bigDecimal = new BigDecimal(Long.MAX_VALUE);
        private LocalDate dates = LocalDate.now();
        private LocalTime times = LocalTime.now();

        public Pikachu() {

        }

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

        public BigDecimal getBigDecimal() {
            return bigDecimal;
        }

        public void setBigDecimal(BigDecimal bigDecimal) {
            this.bigDecimal = bigDecimal;
        }

        public double getSalary() {
            return salary;
        }

        public void setSalary(double salary) {
            this.salary = salary;
        }

        public boolean isSex() {
            return sex;
        }

        public void setSex(boolean sex) {
            this.sex = sex;
        }

        public long getPhone() {
            return phone;
        }

        public void setPhone(long phone) {
            this.phone = phone;
        }

        public LocalDateTime getBirthday() {
            return birthday;
        }

        public void setBirthday(LocalDateTime birthday) {
            this.birthday = birthday;
        }

        public Date getNow() {
            return now;
        }

        public void setNow(Date now) {
            this.now = now;
        }

        public byte[] getBytes() {
            return bytes;
        }

        public void setBytes(byte[] bytes) {
            this.bytes = bytes;
        }

        public String getClobs() {
            return clobs;
        }

        public void setClobs(String clobs) {
            this.clobs = clobs;
        }

        public LocalDate getDates() {
            return dates;
        }

        public void setDates(LocalDate dates) {
            this.dates = dates;
        }

        public LocalTime getTimes() {
            return times;
        }

        public void setTimes(LocalTime times) {
            this.times = times;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Pikachu.class.getSimpleName() + "[", "]")
                    .add("id='" + id + "'")
                    .add("name='" + name + "'")
                    .add("age=" + age)
                    .add("salary=" + salary)
                    .add("sex=" + sex)
                    .add("phone=" + phone)
                    .add("birthday=" + birthday)
                    .add("now=" + now)
                    .add("bytes=" + Arrays.toString(bytes))
                    .add("clobs='" + clobs + "'")
                    .add("bigDecimal=" + bigDecimal)
                    .add("dates=" + dates)
                    .add("times=" + times)
                    .toString();
        }

    }

}
