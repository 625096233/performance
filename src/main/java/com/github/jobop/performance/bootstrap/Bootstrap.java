package com.github.jobop.performance.bootstrap;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.github.jobop.performance.loader.PrerformanceBizSpiLoaderUtil;
import com.github.jobop.performance.parser.ArgusParser;
import com.github.jobop.performance.parser.PerformanceContext;
import com.github.jobop.performance.spi.PerformanceBizSpi;
import com.github.jobop.performance.task.PerformanceTask;

public class Bootstrap {
	private String baseSpiPath = "/usr/local/webapp/performance";

	//
	public void run(String[] args) {

		// ���������� ѹ��ӿ�(��ָ����˳��ȫѹ)n ѹ��ʱ��t �߳���c �����ַl
		PerformanceContext context = new ArgusParser().parse(args);
		System.out.println("�ӿڲ��Բ�������Ϊ \n\r���Գ���ʱ�䣺" + context.getAbidanceTime() + "\n\r" + "ָ������ҵ��(��Ϊȫ��)��" + context.getBizName() + "\n\r" + "��־���·����" + context.getLogPath() + "\n\r" + "worker�߳�����"
				+ context.getThreadCount() + "\n\r");

		PerformanceTask task = new PerformanceTask();
		task.t(context.getAbidanceTime()).n(context.getBizName()).c(context.getThreadCount()).l(context.getLogPath());
		defindLibPath();
		List<PerformanceBizSpi> spiList = loadSpiList();
		// �ѿ����ڲ��Ե�spi����ӵ�������
		for (PerformanceBizSpi spi : spiList) {
			task.addTest(spi);
		}
		task.start();
	}

	private void defindLibPath() {
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(new File(System.getProperty("appHome") + "/" + "config.properties")));
		} catch (IOException e) {
			System.out.println("�����ļ������ڣ�" + System.getProperty("appHome") + "/" + "config.properties");
		}
		baseSpiPath = prop.getProperty("LIB_BASE_PATH");
	}

	private List<PerformanceBizSpi> loadSpiList() {
		List<PerformanceBizSpi> spiList = new ArrayList<PerformanceBizSpi>();
		File baseDir = new File(baseSpiPath);
		if (null == baseDir || !baseDir.exists()) {
			System.out.println("baseDir �����ڣ�baseSpiPath=" + baseSpiPath);
			return spiList;
		}
		File[] subDirs = baseDir.listFiles();
		spiList = PrerformanceBizSpiLoaderUtil.loadSpiFromDir(baseDir);
		for (File subDir : subDirs) {
			if (!subDir.isDirectory()) {
				continue;
			}
			spiList.addAll(PrerformanceBizSpiLoaderUtil.loadSpiFromDir(subDir));
		}
		return spiList;
	}

}
