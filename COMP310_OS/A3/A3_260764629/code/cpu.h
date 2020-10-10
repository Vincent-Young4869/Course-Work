#define DEFAULT_QUANTA 2

struct CPU { 
    int IP; 
    char IR[1000]; 
    int quanta; 
};

int runCPU(int quanta);
void initCPU();
void setCPU(int PC,int offset);
int getCPUpc();
int getCPUoffset();
