package cn.qihangerp.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidator {

    // 密码校验方法
    public static ResultVo validatePassword(String username, String password) {
        // 1. 检查密码长度（至少8位，最多32位）
        if (password.length() < 8 || password.length() > 32) {
//            System.out.println("密码长度必须在 8 到 32 个字符之间");
            return ResultVo.error("密码长度必须在 8 到 32 个字符之间");
        }

        // 2. 检查密码是否包含大小写字母、数字和特殊字符
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>]).+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        if (!matcher.matches()) {
//            System.out.println("密码必须包含大小写字母、数字和特殊字符");
            return ResultVo.error("密码必须包含大小写字母、数字和特殊字符");
        }

        // 3. 禁用连续字符和重复字符（如123456、aaaaaa）
        if (hasSequentialOrRepeatedChars(password)) {
//            System.out.println("密码不能包含连续或重复字符");
            return ResultVo.error("密码不能包含连续或重复字符");
        }

        // 4. 密码不能与用户名相同
        if (password.equalsIgnoreCase(username)) {
            System.out.println("密码不能与用户名相同");
            return  ResultVo.error("密码不能与用户名相同");
        }

        // 校验通过
//        System.out.println("密码设置成功！");
        return ResultVo.success();
    }

    // 检查密码是否包含连续或重复字符
    private static boolean hasSequentialOrRepeatedChars(String password) {
        // 检查是否有连续字符，例如 123456
        for (int i = 0; i < password.length() - 2; i++) {
            char current = password.charAt(i);
            char next = password.charAt(i + 1);
            char nextNext = password.charAt(i + 2);
            // 检查是否为连续数字或字母
            if (next == current + 1 && nextNext == current + 2) {
                return true; // 存在连续字符
            }
        }

        // 检查是否有重复字符，例如 aaaaaa
        for (int i = 0; i < password.length() - 1; i++) {
            char current = password.charAt(i);
            char next = password.charAt(i + 1);
            if (current == next) {
                return true; // 存在重复字符
            }
        }

        return false;
    }

//    public static void main(String[] args) {
//        String username = "user123"; // 假设的用户名
//        String password = "Passw0rd!"; // 假设的密码
//
//        if (validatePassword(username, password)) {
//            System.out.println("密码符合安全要求！");
//        } else {
//            System.out.println("密码不符合安全要求！");
//        }
//    }
}
