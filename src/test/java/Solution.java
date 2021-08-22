public  class Solution {
    public int[] twoSum(int[] nums, int target) {
           return null;
    }

    public static void main(String[] args) {
        int[] nums1={2,7,11,15};
        int[] nums2={3,2,4};
        int[] nums3={3,3};
        Solution solution=new Solution();
        int[] ints = solution.twoSum(nums1, 9);
        if(ints[0]==0 && ints[1]==1 ){
            System.out.println("Test OK!!!!");
        }else {
            System.out.println("Test fail!!!!");
        }
        int[] ints1 = solution.twoSum(nums2, 6);
        if(ints1[0]==1 && ints1[1]==2){
            System.out.println("Test OK!!!!");
        }else {
            System.out.println("Test fail!!!!");
        }
        int[] ints2 = solution.twoSum(nums3, 6);
        if(ints2[0]==0 && ints2[1]==1){
            System.out.println("Test OK!!!!");
        }else{
            System.out.println("Test fail!!!!");
        }
    }
}