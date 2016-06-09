package com.nope.asynchronousengineclient;

import java.util.ArrayList;
import java.util.Arrays;

public class primeCalculator {

    int lowerBound;
    int upperBound;

    primeCalculator(int l, int u) {
        lowerBound = l;
        upperBound = u;
    }

    ArrayList<Integer> calculate() {
        boolean[] primes = new boolean[upperBound];
        Arrays.fill(primes,true);
        primes[0]=primes[1]=false;

        for (int i = 2; i < primes.length; i++) {
            if(primes[i]) {
                for (int j = 2; i * j < primes.length; j++) {
                    primes[i*j]=false;
                }
            }
        }

        ArrayList<Integer> primeNums = new ArrayList<>();
        for (int i = 0; i < primes.length; i++) {
            if (primes[i] && i > lowerBound) {
                primeNums.add(i);
            }
        }
        return primeNums;
    }
}