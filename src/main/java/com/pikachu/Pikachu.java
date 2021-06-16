package com.pikachu;

import com.pikachu.common.annotations.IColumn;
import com.pikachu.common.annotations.ITable;
import com.pikachu.common.collection.Where;
import com.pikachu.framework.database.DaoManager;
import com.pikachu.framework.database.IDao;
import com.pikachu.framework.database.core.DatabaseConfig;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * @Desc
 * @Date 2021/4/21 11:22
 * @Author AD
 */
@ITable(doc = "测试", cache = true, history = true, table = "pikachu")
public class Pikachu implements Serializable {
    
    public static void main(String[] args) throws Exception {
        DatabaseConfig d = new DatabaseConfig();
        d.setName("AD");
        d.setUrl("jdbc:mysql://localhost:3306/study");
        d.setDriver("com.mysql.jdbc.Driver");
        d.setUser("root");
        d.setPassword("123456");
        
        DaoManager daoManager = new DaoManager(d);
        IDao<Pikachu> dao = daoManager.getDao(Pikachu.class);
        Where[] ws = new Where[1];
        List<Object> ages = new ArrayList<>();
        ages.add(1);
        ages.add(2);
        int[] is = new int[2];
        is[0] = 1;
        is[1] = 2;
        
        Integer[] integers = new Integer[2];
        integers[0] = 1;
        integers[1] = 2;
        Set<Enums> enums = new HashSet<>();
        enums.add(Enums.ONE);
        enums.add(Enums.two);
        
        Enums[] enumss = new Enums[2];
        enumss[0]=Enums.ONE;
        enumss[1]=Enums.two;
        ws[0] = new Where("enums", "in", enums);
        Pikachu[] ps = dao.getList(ws, null);
        for (Pikachu p : ps) {
            System.out.println(p);
        }
    
        String[] pks = dao.getPrimaryKeys();
        for (String pk : pks) {
            System.out.println(pk);
        }
    
        int count = dao.delete(new Where[]{new Where("id", "in", "1,2")});
        System.out.println(count);
    
    }
    
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
    @IColumn(doc = "测试", column = "big_decimal")
    private BigDecimal bigDecimal = new BigDecimal(Long.MAX_VALUE);
    private LocalDate dates = LocalDate.now();
    private LocalTime times = LocalTime.now();
    private Enums enums = Enums.two;
    
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
    
    public Enums getEnums() {
        return enums;
    }
    
    public void setEnums(Enums enums) {
        this.enums = enums;
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
                .add("bigDecimal=" + bigDecimal)
                .add("dates=" + dates)
                .add("times=" + times)
                .add("enums=" + enums)
                .toString();
    }
    
}
