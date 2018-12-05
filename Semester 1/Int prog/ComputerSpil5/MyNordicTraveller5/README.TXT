ok, omskriv test og dokumentation for city.arrive(), bonus<0
ok, test og dokumentation til city.arrive(Player p)
tests til equals og hashcode
    city    equalsOK, hashOK
    country equalsOK, hashOK


arriveTest(cityA,cityC,250); /** regular to border, same country1 */
        arriveTest(cityC,cityA,250); /** border to regular, same country1 */
        arriveTest(cityG,cityF,250); /** regular to border, same country2 */
        arriveTest(cityA,cityC,-250); /** regular to border, same country1, negative money */
        arriveTest(cityC,cityA,-250); /** border to regular, same country1, negative money */
        arriveTest(cityG,cityF,-250); /** regular to border, same country2, negative money */
arriveTest(cityE,cityC,250); /** border to border, crossing border */
        arriveTest(cityG,cityC,250); /** regular to border, crossing border */
        arriveTest(cityC,cityG,250); /** border to regular, crossing border */
        arriveTest(cityE,cityC,-250); /** border to border, crossing border, negative money */
        arriveTest(cityG,cityC,-250); /** regular to border, crossing border, negative money */
        arriveTest(cityC,cityG,-250); /** border to regular, crossing border, negative money */
for(int i=-500;i<500;i++){
        arriveTest(cityE,cityC,i); /** border to border, crossing border */
        arriveTest(cityG,cityC,i); /** regular to border, crossing border */
        arriveTest(cityC,cityG,i); /** border to regular, crossing border */
        }