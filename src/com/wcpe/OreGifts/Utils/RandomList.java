package com.wcpe.OreGifts.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RandomList {
	// 随机抽取
		public static int Random(List<Double> TotalChance) {

			if (TotalChance == null || TotalChance.isEmpty()) {
				return -1;
			}

			int size = TotalChance.size();

			// 计算总概率，这样可以保证不一定总概率是1
			double sumRate = 0d;
			for (double rate : TotalChance) {
				sumRate += rate;
			}

			// 计算每个物品在总概率的基础下的概率情况
			List<Double> sortOrignalRates = new ArrayList<>(size);
			Double tempSumRate = 0d;
			for (double rate : TotalChance) {
				tempSumRate += rate;
				sortOrignalRates.add(tempSumRate / sumRate);
			}

			// 得到索引
			double nextDouble = Math.random();
			sortOrignalRates.add(nextDouble);
			Collections.sort(sortOrignalRates);

			return sortOrignalRates.indexOf(nextDouble);
		}
}
