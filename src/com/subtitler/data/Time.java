package com.subtitler.data;

public class Time {
    private int hour;
    private int minute;
    private int second;
    private int millisecond;

    public Time() {

    }

    public Time(int hour, int minute, int second, int millisecond) {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.millisecond = millisecond;
    }

    public Time add(Time time) {
        Time newTime = new Time();

        newTime.addMillisecond(millisecond + time.millisecond);
        validateTime(newTime);
        newTime.addSecond(second + time.second);
        validateTime(newTime);
        newTime.addMinute(minute + time.minute);
        validateTime(newTime);
        newTime.addHour(hour + time.hour);

        return newTime;
    }

    public Time difference(Time time) {
        Time newTime = new Time();

        newTime.addMillisecond(millisecond - time.millisecond);
        newTime.addSecond(second - time.second);
        newTime.addMinute(minute - time.minute);
        newTime.addHour(hour - time.hour);

        return newTime;
    }

    private void validateTime(Time time) {
        if (time.millisecond > 999) {
            time.millisecond -= 1000;
            time.second += 1;
        } else if (time.millisecond < 0) {
            time.millisecond += 1000;
            time.second -= 1;
        }

        if (time.second > 59) {
            time.second -= 60;
            time.minute += 1;
        } else if (time.second < 0) {
            time.second += 60;
            time.minute -= 1;
        }

        if (time.minute > 59) {
            time.minute -= 60;
            time.hour += 1;
        } else if (time.minute < 0) {
            time.minute += 60;
            time.hour -= 1;
        }
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void addHour(int hour) {
        this.hour += hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public void addMinute(int minute) {
        this.minute += minute;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public void addSecond(int second) {
        this.second += second;
    }

    public int getMillisecond() {
        return millisecond;
    }

    public void setMillisecond(int millisecond) {
        this.millisecond = millisecond;
    }

    public void addMillisecond(int millisecond) {
        this.millisecond += millisecond;
    }

    @Override
    public String toString() {
        return String.format("%02d:%02d:%02d,%03d", hour, minute, second, millisecond);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Time time = (Time) o;

        return hour == time.hour && minute == time.minute && second == time.second && millisecond == time.millisecond;
    }

    @Override
    public int hashCode() {
        int result = hour;
        result = 31 * result + minute;
        result = 31 * result + second;
        result = 31 * result + millisecond;
        return result;
    }
}