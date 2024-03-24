import java.util.HashSet;

public class Cryptarithm {

    static int c = 1;
    static int y = 1;
    static int l = 0;
    static int i = 0;
    static int n = 1;
    static int d = 9;
    static int e = 1;
    static int r = 1;
    static int g = 0;
    static int z = 0;

    static boolean condition(int ct, int yt, int lt, int it, int nt, int dt, int et, int rt, int gt, int zt) {
        HashSet<Integer> uniqueValues = new HashSet<>();
        uniqueValues.add(ct);
        uniqueValues.add(yt);
        uniqueValues.add(lt);
        uniqueValues.add(it);
        uniqueValues.add(nt);
        uniqueValues.add(dt);
        uniqueValues.add(et);
        uniqueValues.add(rt);
        uniqueValues.add(gt);
        uniqueValues.add(zt);
        return (ct * 10000000 + yt * 1000000 + lt * 100000 + it * 10000 + nt * 1000 + dt * 100 + et * 10 + rt +
                et * 10000000 + nt * 1000000 + et * 100000 + rt * 10000 + gt * 1000 + it * 100 + zt * 10 + et
                ==
                dt * 10000000 + rt * 1000000 + it * 100000 + lt * 10000 + lt * 1000 + it * 100 + nt * 10 + gt
                && uniqueValues.size() == 10);
    }

    static void findAnswer() {
        for (int ct = c; ct < 10; ct++) {
            for (int yt = y; yt < 10; yt++) {
                for (int lt = l; lt < 10; lt++) {
                    for (int it = i; it < 10; it++) {
                        for (int nt = n; nt < 10; nt++) {
                            for (int et = e; et < 10; et++) {
                                for (int rt = r; rt < 10; rt++) {
                                    for (int gt = g; gt < 10; gt++) {
                                        for (int zt = z; zt < 10; zt++) {
                                            if (condition(ct, yt, lt, it, nt, d, et, rt, gt, zt)) {
                                                System.out.println(ct + " " + yt + " " + lt + " " + it + " " + nt + " " + d + " " + et + " " + rt + " " + gt + " " + zt);
                                                return;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        findAnswer();
    }
}
