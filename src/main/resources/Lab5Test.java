class Range {
    static final int min = 0;
    static final int MAX = 100;

    final int Min;
    final int Max;

    Range() {
        this.Min = min;
        this.Max = MAX;
    }

    Range(int min, int max) {
        this.Min = min;
        this.Max = max;
    }
}

class test {
    static boolean isNegative(int n) {
        if(n < 0) {
            return true;
        }
        else {

        }
        return false;
    }

    static boolean isPositive(int n) {
        if(isNegative(n) == true) {
            return false;
        }
        else {
            return true;
        }
    }

    static int Abs(int n) {
        return n < 0 ? -n : n;
    }

    static int abs2(int n) {
        if(n < 0)
            n = -n;
        else
            n = n;
        return n;
    }

    static int fact(int N) {
        if (N == 1) {
            return 1;
        }
        else {
            return N * fact(N - 1);
        }
    }

    static boolean isEven(int n) {
        if (n % 2 == 0)
            return true;
        else
            return false;
    }
}