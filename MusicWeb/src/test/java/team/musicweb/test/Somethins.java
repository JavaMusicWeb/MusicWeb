package team.musicweb.test;

import java.util.LinkedList;
import java.util.List;

import team.musicweb.moudle.MusicSheet;

public class Somethins {
	public static void main(String[] args) {
		
		MusicSheet musicSheet=new MusicSheet();
		List<String> lStrings=new LinkedList<String>();
		lStrings.add("456413");
		lStrings.add("666");
		musicSheet.setMusics(lStrings);
		musicSheet.getMusics().remove("666");
		for (String string : musicSheet.getMusics()) {
			System.out.println(string);
		}
	}
	public static void name(Integer aa) {
		try {
			return;
		} catch (Exception e) {
			// TODO: handle exception
		}
		finally {
		System.out.println(aa=5);	
		}
	}
}
