/*package com.jery.feedformulation.utils;

import android.util.Log;

import com.jery.feedformulation.data.Feed;
import com.jery.feedformulation.data.Nutrients;

import java.util.ArrayList;
import java.util.List;

public class Simplex {

    Nutrients nutirents;
    List<Feed> feeds;

    Double[][] A;
    int[] M;
    int m, n;
    Double[] ans;

    double total_dm = 0.0, total_cp = 0.0, total_tdn = 0.0;

    static Double FeedValues[][] = {
            {90.0, 3.5, 40.0, 0.18, 0.08, 20.0},
            {20.0, 8.0, 52.0, 0.38, 0.36, 40.0},
            {20.0, 8.0, 60.0, 0.53, 0.14, 40.0},
            {90.0, 7.0, 50.0, 0.3, 0.25, 10.0},
            {90.0, 6.0, 42.0, 0.15, 0.09, 10.0},
            {20.0, 15.8, 60.0, 1.44, 0.14, 10.0},
            {90.0, 3.3, 42.0, 0.3, 0.06, 10.0},
            {90.0, 3.0, 42.0, 0.53, 0.14, 40.0},
            {90.0, 8.1, 79.2, 0.53, 0.41, 10.0},
            {90.0, 42.0, 70.0, 0.36, 1.0, 10.0},
            {90.0, 22.0, 70.0, 0.2, 0.9, 10.0},
            {90.0, 32.0, 70.0, 0.31, 0.72, 10.0},
            {75.0, 12.0, 70.0, 1.067, 0.093, 10.0},
            {90.0, 17.0, 70.0, 0.28, 0.54, 10.0},
            {90.0, 16.0, 110.0, 0.3, 0.62, 10.0},
            {90.0, 18.0, 45.0, 0.3, 0.62, 10.0},
            {90.0, 22.0, 70.0, 0.5, 0.45, 10.0},
            {90.0, 20.0, 65.0, 0.5, 0.4, 10.0},
            {97.0, 0.0, 0.0, 36.0, 0.0, 1.0},
            {96.0, 0.0, 0.0, 36.0, 0.0, 1.0},
            {90.0, 0.0, 0.0, 32.0, 15.0, 0.1},
            {90.0, 0.0, 0.0, 24.0, 16.0, 1.0},
            {90.0, 0.0, 0.0, 0.0, 0.0, 0.0},
            {90.0, 0.0, 0.0, 0.0, 0.0, 0.5},
            {98.0, 0.0, 0.0, 0.0, 0.0, 0.0},
            {95.0, 287.5, 0.0, 0.0, 0.0, 0.2}};
    static Double percent[] = {20.0,40.0,40.0,10.0,10.0,10.0,10.0,40.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,1.0,1.0,0.1,1.0,0.0,0.5,0.0,0.2};

    void Operate(int a, int b) {
        double p = 0.0;

        Double B[][] = new Double[this.m+1][this.n+1];
        for(int i=0; i<this.m+1; i++)
            for(int j=0; j<this.n+1; j++)
                B[i][j] =this.A[i][j];

        p = B[a][b];
        this.A[a][b] = 1/p;
        for(int i=0; i<this.m+1; i++) {
            if(i != a) {
                this.A[i][b] = -1*B[i][b]/p;
            }
        }

        for(int j=0; j<this.n+1; j++) {
            if(j != b) {
                this.A[a][j] = B[a][j]/p;
            }
        }
        for(int i=0; i<this.m+1; i++) {
            for(int j=0; j<this.n+1; j++) {
                if((i!=a) && (j!=b)) {
                    this.A[i][j] = ((B[a][b]*B[i][j])-(B[a][j]*B[i][b]))/p;
                }
            }
        }
    }

    double Primal(int im) {
        int J = this.n, I = 0;
        double min = 0.0, PI = 0.0;

        for(int j=0; j<this.n; j++) {
            if(this.A[this.m][j] < min) {
                min = this.A[this.m][j];
                J = j;
            }
        }

        double sum = -1.0;
        double ratio = 0.0;
        for(int i=0; i<this.m; i++) {
            if(this.A[i][J] > 0) {
                ratio = this.A[i][this.n]/this.A[i][J];
                if(sum == -1.0 || sum > ratio) {
                    sum = ratio;
                    I = i;
                }
            }
        }

        if(im == 1) {
            PI = this.A[I][this.n]*this.A[this.m][J]/this.A[I][J];
            if(PI < 0) {
                PI *= -1;
            }
            return PI;
        }
        this.Operate(I, J);
        int temp = this.M[I+this.n];
        this.M[I+this.n] = this.M[J];
        this.M[J] = temp;
        return 0;
    }

    double Dual(int im) {
        int I = this.m, J = 0;
        double min = 0.0, DI = 0.0;

        for(int i=0; i<this.m; i++) {
            if(this.A[i][this.n] < min) {
                min = this.A[i][this.n];
                I = i;
            }
        }

        double sum = -1.0;
        double ratio = 0.0;
        for(int j=0; j<this.n; j++) {
            if(this.A[I][j] < 0) {
                ratio = this.A[this.m][j]/this.A[I][j];
                if(sum == -1.0 || sum > ratio) {
                    sum = ratio;
                    J = j;
                }
            }
        }

        if(im == 1) {
            DI = this.A[I][this.n]*this.A[this.m][J]/this.A[I][J];
            if(DI < 0) {
                DI *= -1;
            }
            return DI;
        }
        this.Operate(I, J);
        int temp = this.M[I+this.n];
        this.M[I+this.n] = this.M[J];
        this.M[J] = temp;
        return 0;
    }

    void simplex() {
        int c1 = 0, c2 = 0;
        for(int i=0; i<this.m; i++) {
            if(this.A[i][this.n] < 0) {
                c1 = 1;
            }
        }

        for(int i=0; i<this.n; i++) {
            if(this.A[this.m][i] < 0) {
                c2 = 1;
            }
        }

        if(c1 == 0 && c2 == 0)
            return;
        else if(c1 == 0)
            this.Primal(0);
        else if(c2 == 0)
            this.Dual(0);
        else {
            double PI = this.Primal(1);
            double DI = this.Dual(1);
            if(PI >= DI)
                this.Primal(0);
            else
                this.Dual(0);
        }
        this.simplex();

    }

    void Result() {
        this.ans = new Double[this.n];
        for(int i=0; i<this.n; i++)
            this.ans[i] = 0.0;
        for(int i=0; i<this.m; i++) {
            if(this.M[i+this.n] < this.n) {
                this.ans[this.M[i+this.n]] = this.A[i][n];
            }
        }
    }

    void CreateEquations(ArrayList<Integer> feeds, ArrayList<Double> values) {
        this.n = feeds.size();
        this.m = this.n+10;

        this.M = new int[this.m+this.n];
        for(int i=0; i<this.m+this.n; i++) {
            this.M[i] = i;
        }

        this.A = new Double[this.m+1][this.n+1];
        for(int i=0; i<this.m+1; i++)
            for(int j=0; j<this.n+1; j++)
                this.A[i][j] = 0.0;

        for(int i=0; i<this.n; i++) {
            this.A[0][i] = FeedValues[feeds.get(i)][0];
            this.A[1][i] = -1*FeedValues[feeds.get(i)][0];
        }
        this.A[0][this.n] = this.nutirents.getDm()*100;
        this.A[1][this.n] = -1*this.nutirents.getDm()*100;

        for(int i=0; i<this.n; i++) {
            this.A[2][i] = FeedValues[feeds.get(i)][1];
            this.A[3][i] = -1*FeedValues[feeds.get(i)][1];
        }
        this.A[2][this.n] = this.nutirents.getCp()/10*1.1;
        this.A[3][this.n] = -1*this.nutirents.getCp()/10;

        for(int i=0; i<this.n; i++) {
            this.A[4][i] = FeedValues[feeds.get(i)][2];
            this.A[5][i] = -1*FeedValues[feeds.get(i)][2];
        }
        this.A[4][this.n] = this.nutirents.getTdn()/10*1.1;
        this.A[5][this.n] = -1*this.nutirents.getTdn()/10;

        for(int i=0; i<this.n; i++) {
            if(feeds.get(i) < 8) {
                this.A[6][i] = 1.0;
                this.A[7][i] = -1.0;
                this.A[8][i] = 0.0;
                this.A[9][i] = 0.0;
            } else {
                this.A[6][i] = 0.0;
                this.A[7][i] = 0.0;
                this.A[8][i] = 1.0;
                this.A[9][i] = -1.0;
            }
        }
        this.A[6][n] = this.nutirents.getDm()*0.8;
        this.A[7][n] = -1*this.nutirents.getDm()*0.4;
        this.A[8][n] = this.nutirents.getDm()*0.7;
        this.A[9][n] = -1*this.nutirents.getDm()*0.2;

        for(int i=0; i<this.n; i++) {
            this.A[10+i][i] = 1.0;
            this.A[10+i][this.n] = percent[feeds.get(i)]*this.nutirents.getDm()/100;
        }

        for(int i=0; i<this.n; i++) {
            this.A[this.m][i] = values.get(i);
        }

    }

    public void Solver(List<Feed> feedsList) {
        this.CreateEquations(feeds, );

        Log.v("Nutrients", this.nutirents.getDm() + " -- " + this.nutirents.getCp() + " -- " + this.nutirents.getTdn());

        this.simplex();
        this.Result();

        for(int i=0; i<this.n; i++) {
            this.total_dm += (FeedValues[this.feedData.feedsSelected.get(i)][0] * this.ans[i]);
            this.total_cp += (FeedValues[this.feedData.feedsSelected.get(i)][1] * this.ans[i]);
            this.total_tdn += (FeedValues[this.feedData.feedsSelected.get(i)][2] * this.ans[i]);
        }
    }

    Simplex() {
        this.nutirents = Nutrients.getInstance();
        this.feedData = FeedData.getInstance();
    }

}*/