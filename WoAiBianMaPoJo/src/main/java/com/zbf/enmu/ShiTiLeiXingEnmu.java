package com.zbf.enmu;

/**
 * 作者：LCG
 * 创建时间：2019/2/17 13:54
 * 描述：
 */
public enum  ShiTiLeiXingEnmu {

      DAN_XUAN("1","单选题"),FU_XUAN("2","多选题"),PAN_DUAN("3","判断题"),TIAN_KONG("4","填空题"),WEN_DA("5","问答题");

      private String id;

      private String value;

      private ShiTiLeiXingEnmu(String id,String value){
          this.id=id;
          this.value=value;
      }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
