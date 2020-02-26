package io.ssosso.rest;

import io.ssosso.rest.events.EventRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

//@SpringBootTest
@RunWith(SpringRunner.class)
class RestApplicationTests {


	//  @Autowired
	EventRepository eventRepository;

	@org.junit.Test
	public void contextLoads() {
		System.out.println("eventRepository = " + eventRepository);
	}

	@org.junit.Test
	public void test1() {
		System.out.println("dd");

		int n = 12;
		int sum = 0;
		for (int i = 1; i <= n; i++) {
			if (n % i == 0) {
				sum += i;
			}
		}
		System.out.println("sum = " + sum);
	}

	@org.junit.Test
	public void test2() {
//    String s = "a234";
		String s = "1234";
		boolean answer = true;


//    s.chars().filter(i -> i<48 || i>57).count() > 0 ? false : true;



//    if (s.matches(".*[^1-9].*")) {
//      System.out.println("answer = false");
//    } else{
//      System.out.println("answer = true");
//    }



//      if (!Character.isDigit(s.charAt(i))) {
//        answer = false;
//        break;
//      }
//      if (!Character.isDigit(s.charAt(s.length() - i))) {
//        answer = false;
//        break;
//      }
	}


	@Test
	public void test3() {
		int[] d = {2, 2, 3, 3};
//    int[] d = {1, 3, 2, 5, 4};
		int budget = 10;

		Arrays.sort(d);
		Arrays.stream(d).forEach(System.out::println);
		System.out.println("========");

		int max = d[d.length - 1];
		int a = 0;
		for (int i = 0; i < d.length; i++) {
			a += d[i];
			if ((a) <= budget && (a + max) <= budget) {
				continue;
			}
			System.out.println(i+1);
			break;
		}

	}

}
