package com.zinkworks.assessment.controller.dto;

public class Notes {
  private Integer faceNotes;
  private Integer quantity;
  
  public Notes(Integer faceNotes, Integer quantity) {
    this.faceNotes = faceNotes;
    this.quantity = quantity;
  }
  
  public Integer getFaceNotes() {
    return faceNotes;
  }
  
  public Integer getQuantity() {
    return quantity;
  }
}
