import com.wonderland.craftor.HandmadeMaster
import org.scalatest.FunSuite

class ThiefTester extends FunSuite{
  test("HandmadeMaster.config") {
    val example = new HandmadeMaster(Array("1", "2"))
    assert(example.config == "1")
  }
}
