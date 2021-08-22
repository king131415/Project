package DAO;

import Utils.DButil;
import model.Problem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProblemDao {
    public void insert(Problem problem){
        Connection connection= DButil.getConnection();
        PreparedStatement statement=null;

        String sql="insert into problem_table value(null,?,?,?,?,?)";
        try {
            statement=connection.prepareStatement(sql);
            statement.setString(1,problem.getTitle());
            statement.setString(2,problem.getLevel());
            statement.setString(3,problem.getDescription());
            statement.setString(4,problem.getTemplatecode());
            statement.setString(5,problem.getTestCode());

            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            DButil.close(connection,statement,null);
        }
    }
    public void delete(int problemId){
        Connection connection=DButil.getConnection();
        PreparedStatement statement=null;

        String sql="delete from oj_table where id=?";

        try {
            statement=connection.prepareStatement(sql);
            statement.setInt(1,problemId);
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            DButil.close(connection,statement,null);
        }

    }
    public List<Problem> selectAll(){
        List<Problem> problems=new ArrayList<>();
        Connection connection=DButil.getConnection();
        PreparedStatement statement=null;
        ResultSet resultSet=null;

        String sql="select id,title,level from oj_table";

        try {
            //执行预编译命令
            statement =connection.prepareStatement(sql);
            //执行SQl
            resultSet=statement.executeQuery();
            //遍历结果集

            while (resultSet.next()){
                Problem problem=new Problem();
                problem.setId(resultSet.getInt("id"));
                problem.setTitle(resultSet.getString("title"));
                problem.setLevel(resultSet.getString("level"));
                problems.add(problem);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            DButil.close(connection,statement,resultSet);
        }
        return problems;
    }
    public Problem selectOne(int problemId){
        Connection connection=DButil.getConnection();
        PreparedStatement statement=null;
        ResultSet resultSet=null;
        String sql="select * from oj_table where id=?";

        try {
            //执行预处理命令
             statement=connection.prepareStatement(sql);
             //替换占位符
             statement.setInt(1,problemId);
             //执行SQl
             resultSet = statement.executeQuery();

             if(resultSet.next()){
                 Problem problem=new Problem();
                 problem.setId(resultSet.getInt("id"));
                 problem.setTitle(resultSet.getString("title"));
                 problem.setLevel(resultSet.getString("level"));
                 problem.setDescription(resultSet.getString("description"));
                 problem.setTemplatecode(resultSet.getString("templatecode"));
                 problem.setTestCode(resultSet.getString("testCode"));
                 return problem;
             }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            DButil.close(connection,statement,resultSet);
        }
        return null;
    }
    private static void testInsert(){
        Problem problem=new Problem();
        problem.setId(3);
        problem.setTitle("两数之和");
        problem.setLevel("简单");
        problem.setDescription("给定一个整数数组 nums 和一个整数目标值 target，请你在该数组中找出 和为目标值 target  的那 两个 整数，并返回它们的数组下标。\n" +
                "\n" +
                "你可以假设每种输入只会对应一个答案。但是，数组中同一个元素在答案里不能重复出现。\n" +
                "\n" +
                "你可以按任意顺序返回答案。\n" +
                "\n" +
                " \n" +
                "\n" +
                "示例 1：\n" +
                "\n" +
                "输入：nums = [2,7,11,15], target = 9\n" +
                "输出：[0,1]\n" +
                "解释：因为 nums[0] + nums[1] == 9 ，返回 [0, 1] 。\n" +
                "示例 2：\n" +
                "\n" +
                "输入：nums = [3,2,4], target = 6\n" +
                "输出：[1,2]\n" +
                "示例 3：\n" +
                "\n" +
                "输入：nums = [3,3], target = 6\n" +
                "输出：[0,1]\n" +
                " \n" +
                "\n" +
                "提示：\n" +
                "\n" +
                "2 <= nums.length <= 104\n" +
                "-109 <= nums[i] <= 109\n" +
                "-109 <= target <= 109\n" +
                "只会存在一个有效答案\n" +
                "进阶：你可以想出一个时间复杂度小于 O(n2) 的算法吗？\n" +
                "\n" +
                "通过次数2,250,630提交次数4,364,115\n" +
                "请问您在哪类招聘中遇到此题？\n" +
                "\n" +
                "社招\n" +
                "\n" +
                "校招\n" +
                "\n" +
                "实习\n" +
                "\n" +
                "未遇到\n" +
                "\n" +
                "\n" +
                "来源：力扣（LeetCode）\n" +
                "链接：https://leetcode-cn.com/problems/two-sum\n" +
                "著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。");
        problem.setTemplatecode("class Solution {\n" +
                "    public int[] twoSum(int[] nums, int target) {\n" +
                "\n" +
                "    }\n" +
                "}");
        problem.setTestCode("public static void main(String[] args) {\n" +
                "        int[] nums1={2,7,11,15};\n" +
                "        int[] nums2={3,2,4};\n" +
                "        int[] nums3={3,3};\n" +
                "        Solution solution=new Solution();\n" +
                "        int[] ints = solution.twoSum(nums1, 9);\n" +
                "        if(ints[0]==0 && ints[1]==1 ){\n" +
                "            System.out.println(\"Test OK!!!!\");\n" +
                "        }else {\n" +
                "            System.out.println(\"Test fail!!!!\");\n" +
                "        }\n" +
                "        int[] ints1 = solution.twoSum(nums2, 6);\n" +
                "        if(ints1[0]==1 && ints1[1]==2){\n" +
                "            System.out.println(\"Test OK!!!!\");\n" +
                "        }else {\n" +
                "            System.out.println(\"Test fail!!!!\");\n" +
                "        }\n" +
                "        int[] ints2 = solution.twoSum(nums3, 6);\n" +
                "        if(ints2[0]==0 && ints2[1]==1){\n" +
                "            System.out.println(\"Test OK!!!!\");\n" +
                "        }else{\n" +
                "            System.out.println(\"Test fail!!!!\");\n" +
                "        }\n" +
                "    }");
        ProblemDao problemDao1=new ProblemDao();
        problemDao1.insert(problem);
    }

    public static void main(String[] args) {
        testInsert();
    }
}
