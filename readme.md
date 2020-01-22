
## Android App for checking if subway is faster or taxi is faster(for Seoul).
 지하철 타는 동안 급해서 택시로 갈아타야할지 고민할 때 필요한 로직. 택시로 환승할까 고민 중인 역과 도착지점에서
택시가 빠른지 지하철 시간이 빠른지 계산 후 결과 도출. 
 



# <logic> : 
1. spinners for checking the start location and end location
  - used SQLite to connect DB
2. compared Tmap API for taxi time, ODSay API for subway time
  - used HTTPThread
3. show results by map path
4. added history and favorites UI
