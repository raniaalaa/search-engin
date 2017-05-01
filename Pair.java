public class Pair<E1,E2> {
    private  E1 first;
    private  E2 second;

    public Pair(E1 first, E2 second) {
        this.first = first;
        this.second = second;
    }

    public E1 getFirst()  { return first;  }
    public E2 getSecond() { return second; }
}
/*public class Pair {
	private Integer integer;

	  private String string;
	  public String getstring()
	  {
		  return string;
	  }
	  public Integer getinteger()
	  {
		  return integer;
	  }
}*/
