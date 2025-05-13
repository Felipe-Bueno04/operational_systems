package com.aps;

public abstract class NoGrafo {
  public abstract String getIdentificador();

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof NoGrafo) {
      return ((NoGrafo)obj).getIdentificador().equals(this.getIdentificador());
    }

    else return false;
  }
}
