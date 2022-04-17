package com.chinaums.ysmktaln.spbillserv.common.Exception;

import org.apache.commons.lang.StringUtils;

public enum HandleCode {
   RSP_0000("0000", "成功"),
   RSP_1001("1001", "没有活动"),
   RSP_1002("1002", "使用条件不满足"),
   RSP_1003("1003", "卡未注册"),
   RSP_1004("1004", "没有活动"),
   RSP_1101("1101", "已使用"),
   RSP_1102("1102", "可用次数不足"),
   RSP_1103("1103", "已过期"),
   RSP_1104("1104", "券号状态异常"),
   RSP_1105("1105", "商户不能使用该码"),
   RSP_1106("1106", "终端不能使用该码"),
   RSP_1107("1107", "该时间段不能使用该码"),
   RSP_1108("1108", "使用条件不满足"),
   RSP_1109("1109", "券码不存在"),
   RSP_1110("1110", "没有交易金额"),
   RSP_1111("1111", "交易金额错误"),
   RSP_1201("1201", "卡号错误"),
   RSP_1202("1202", "密码错误"),
   RSP_1203("1203", "会员状态异常"),
   RSP_1204("1204", "积分余额不足"),
   RSP_1205("1205", "积分操作失败"),
   RSP_2001("2001", "手机号已绑定或者银行卡已注册"),
   RSP_2002("2002", "卡号有误"),
   RSP_2003("2003", "渠道号不存在"),
   RSP_2004("2004", "手机号码有误"),
   RSP_2101("2101", "渠道号不存在"),
   RSP_2102("2102", "手机号码有误"),
   RSP_2103("2103", "密码错误"),
   RSP_2104("2104", "服务暂未开启"),
   RSP_2105("2105", "终端号未找到"),
   RSP_2106("2106", "该银行卡已被绑定"),
   RSP_2107("2107", "非有效用户，不允许签到"),
   RSP_2108("2108", "非特许账号，不允许签到"),
   RSP_2109("2109", "非本商户收银员"),
   RSP_2110("2110", "用户状态异常"),
   RSP_2201("2201", "活动券无法绑定和解绑"),
   RSP_3001("3001", "业务错误"),
   RSP_9001("9001", "对应查询流水号已销券"),
   RSP_9002("9002", "已被其他销券请求销券"),
   RSP_9003("9003", "查询流水号不存在"),
   RSP_9004("9004", "查询流水号不正确"),
   RSP_9005("9005", "原查询流水号不正确"),
   RSP_9006("9006", "关联流水号状态异常"),
   RSP_9101("9101", "销券流水号已完成冲正"),
   RSP_9102("9102", "已被其他冲正请求冲正"),
   RSP_9103("9103", "销券流水号不存在"),
   RSP_9110("9110", "商户冻结"),
   RSP_9111("9111", "门店冻结"),
   RSP_9112("9112", "终端未登记"),
   RSP_9113("9113", "交易金额异常"),
   RSP_9114("9114", "无效渠道号"),
   RSP_9115("9115", "无效特征码"),
   RSP_9201("9201", "销券流水号已完成撤销"),
   RSP_9202("9202", "销券流水号未找到"),
   RSP_9203("9203", "超时，销券流水号已清算"),
   RSP_9204("9204", "已被其他撤消请求撤消"),
   RSP_9299("9299", "找到原充值流水"),
   RSP_9301("9301", "密码错误"),
   RSP_9302("9302", "积分不足"),
   RSP_9303("9303", "活动总额度领用完毕"),
   RSP_9304("9304", "当日活动额度领用"),
   RSP_9305("9305", "用户总额度领用完毕"),
   RSP_9306("9306", "当日用户额度领用完毕"),
   RSP_9307("9307", "无效活动"),
   RSP_9308("9308", "无效用户"),
   RSP_9401("9401", "无效订单"),
   RSP_9981("9981", "重复流水号"),
   RSP_9991("9991", "mac错误"),
   RSP_9992("9992", "参数不足"),
   RSP_9993("9993", "报文异常"),
   RSP_9994("9994", "其它错误"),
   RSP_9996("9996", "参数值出错"),
   RSP_9997("9997", "交易代码不存在"),
   RSP_9998("9998", "第三方连接出错"),
   RSP_9999("9999", "系统错误"),
   RSP_CMB_97("97", "系统参考号列表长度错误"),
   RSP_CMB_08("08", "电子凭证已过期或未生效"),
   RSP_CMB_07("07", "电子凭证不可用"),
   RSP_CMB_04("04", "活动信息不存在"),
   RSP_CMB_0002("0002", "该券码之前已被撤销"),
   RSP_CMB_0001("0001", "该券码之前已被使用"),
   RSP_CMB_0003("0003", "该券码之前已过期"),
   RSP_CMB_0004("0004", "券码信息不正确"),
   RSP_CMB_0009("0009", "不存在券码信息");

   private static final String HEAD = "RSP_";
   private String code;
   private String desc;

   private HandleCode(String code, String desc) {
      this.code = code;
      this.desc = desc;
   }

   public String code() {
      String temp = this.name().replace("RSP_", "");
      return temp;
   }

   public String desc() {
      return this.desc;
   }

   public String desc(String desc) {
      return !StringUtils.isEmpty(desc) ? desc : this.desc;
   }

   public static boolean contains(String code) {
      if (StringUtils.isEmpty(code)) {
         return false;
      } else {
         String tempCode = "RSP_" + code;
         HandleCode[] handles = values();
         HandleCode[] var3 = handles;
         int var4 = handles.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            HandleCode handleCode = var3[var5];
            if (tempCode.equals(handleCode.name())) {
               return true;
            }
         }

         return false;
      }
   }

   public static HandleCode get(String code, HandleCode... codes) {
      if (!StringUtils.isEmpty(code)) {
         HandleCode[] handles = values();
         String tempCode = "RSP_" + code;
         HandleCode[] var4 = handles;
         int var5 = handles.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            HandleCode handleCode = var4[var6];
            if (tempCode.equals(handleCode.name())) {
               return handleCode;
            }
         }
      }

      return codes != null && codes.length != 0 ? codes[0] : RSP_9999;
   }
}
