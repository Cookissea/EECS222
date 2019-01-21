#include <stdio.h>
#include <stdlib.h>
#include <string.h>

interface S{
	void send(char x);
};

interface R{
	char receive();
};

channel C
	implements S,R{
		event req;
		event ack;
		char buf;

		void send(char x){
			buf = x;
			notify req;
			wait ack;
		}

		char receive(){
			char y;
			wait req;
			y = buf;
			notify ack;
			return y;
		}
};

behavior Prod(S s){
	const char *str = "Beans and Potatoes";
	int cnt = 0;

	void main(){
		if(cnt == 0){
			printf("Producer starts.\n");cnt++;
		}
		if(*str != '\0'){
			s.send(*str);
			printf("Producer sends %c.\n",*str++);			
		}
		else
			exit(EXIT_FAILURE);	
	}
};

behavior Cons(R r){
	void main(){
		const char *str;
		int cnt;
		int l;
		str = "Beans and Potatoes";
		l = strlen(str);
		cnt = 0;

		if(cnt == 0)
			printf("Consumer starts.\n");
		//char x = s.receive();
		if(cnt != l){
			printf("Consumer sends %c.\n",r.receive());
			cnt++;
		}
		else
			exit(EXIT_FAILURE);	
	}
};

behavior Main{
	C c;
	Prod p(c);
	Cons co(c);
  
	int main()
	{
		/* code */	
		const char *str;
     	int l;
		int i;
		str = "Beans and Potatoes";
		l = strlen(str);

		printf("Main starts.\n");
		//printf("Producer starts.\n");
		//printf("Consumer starts.\n"); 
		for(i = 0;i < l;i++){
			par{
				p;co;
			}
		}
		printf("Consumer done.\n"); 
		printf("Producer done.\n"); 
		printf("Main done.\n");
		return 0;
	}
};