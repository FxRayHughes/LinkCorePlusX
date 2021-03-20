package ray.mintcat.linkcoreplus.attribute

import io.izzel.taboolib.util.Features
import org.serverct.ersha.api.annotations.AutoRegister
import org.serverct.ersha.api.component.DescriptionRead
import ray.mintcat.linkcoreplus.utils.Helper
import ray.mintcat.linkcoreplus.utils.Utils

@AutoRegister
class ReadLore : DescriptionRead(
    ReadPriority.BEFORE, AttributeReadType.VALUE, true
), Helper {

    override fun read(key: String, lore: String): Array<Number> {
        // +10 法术能力 (+20)
        if (lore.indexOf("I") != -1 || lore.indexOf("V") != -1 || lore.indexOf("X") != -1
            || lore.indexOf("L") != -1 || lore.indexOf("C") != -1 || lore.indexOf("D") != -1 || lore.indexOf("M") != -1
        ) {
            val info = lore.replace("[^IVXLCDM]".toRegex(), "")
            return arrayOf(Utils.toInt(info), Utils.toInt(info))
        }
        if (lore.replace("[^+]".toRegex(), "").contains("++")) {
            val number =
                (Features.compileScript(lore.replace("[^0-9+-.*/]".toRegex(), ""))?.eval() ?: 0.0).toString().toDouble()
            return arrayOf(number, number)
        }
        return arrayOf(0.0, 0.0)
    }

}
