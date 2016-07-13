package com.example;

public class Reservation {

    private Long id;
    private String reservationName;
    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }
    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }
    /**
     * @return the reservationName
     */
    public String getReservationName() {
        return reservationName;
    }
    /**
     * @param reservationName the reservationName to set
     */
    public void setReservationName(String reservationName) {
        this.reservationName = reservationName;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Reservation [id=" + id + ", reservationName=" + reservationName
                + "]";
    }
}
