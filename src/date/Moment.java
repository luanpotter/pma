package date;

public class Moment {
  private Date date;
  private Time time;

  public Moment() {
    this(java.util.Calendar.getInstance());
  }
  public Moment(java.util.Calendar currentTime) {
    this.date = new Date(currentTime.get(java.util.Calendar.YEAR), currentTime.get(java.util.Calendar.MONTH) + 1, currentTime.get(java.util.Calendar.DAY_OF_MONTH));
    this.time = new Time(currentTime.get(java.util.Calendar.HOUR_OF_DAY), currentTime.get(java.util.Calendar.MINUTE));
  }

  public Date getDate() {
    return this.date;
  }

  public Time getTime() {
    return this.time;
  }

  @Override
  public String toString() {
    return date.toString() + '+' + time.toString();
  }
}