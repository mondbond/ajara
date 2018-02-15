package managers;

import entity.Author;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.Whitebox;
import org.powermock.api.support.membermodification.MemberModifier;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import repository.AuthorFacade;
import repository.AuthorHome;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ AuthorManager.class })
public class AuthorManagerTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorManagerTest.class);


    private AuthorManager authorManager;

    @BeforeClass
    public void setUpClass(){
        authorManager = new AuthorManager();
    }

    @BeforeMethod
    public void setUp() throws Exception {
        LOGGER.info("setUp");
    }

    @AfterMethod
    public void tearDown() throws Exception {
        LOGGER.info("tearDown");
        authorManager = new AuthorManager();
    }

    @Test(description = "Test if this method return entity with same id as in parameter")
    public void testGetAuthorByPk() throws Exception {
        Long expected = 100L;

        AuthorFacade facadeMock = mock(AuthorFacade.class);
        when(facadeMock.findByPk(expected)).thenReturn(Author.builder().id(expected).build());

        MemberModifier
                .field(AuthorManager .class, "authorFacade").set(
                authorManager , facadeMock);

        Long actual = authorManager.getAuthorByPk(expected).getId();

        verify(facadeMock, times(1)).findByPk(expected);
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testGetAuthorsByPk() throws Exception {
        ArrayList<Long> params = new ArrayList<>();
        params.add(234L);
        params.add(189L);
        params.add(111L);
        params.add(9999L);

        ArrayList<Author> expectedResults = new ArrayList<>();
        expectedResults.add(Author.builder().id(234L).build());
        expectedResults.add(Author.builder().id(189L).build());
        expectedResults.add(Author.builder().id(111L).build());
        expectedResults.add(Author.builder().id(9999L).build());
        AuthorFacade facadeMock = mock(AuthorFacade.class);

        when(facadeMock.findByPks(params)).thenReturn(expectedResults);
        Whitebox.setInternalState(authorManager, "authorFacade", facadeMock);

        List<Author> actual = authorManager.getAuthorsByPk(params);

        verify(facadeMock, times(1)).findByPks(params);
        Assert.assertEquals(actual, expectedResults);
    }

    @Test
    public void testGetAllAuthors() throws Exception {

        ArrayList<Author> expectedResults = new ArrayList<>();
        expectedResults.add(Author.builder().id(234L).build());
        expectedResults.add(Author.builder().id(189L).build());
        expectedResults.add(Author.builder().id(111L).build());
        expectedResults.add(Author.builder().id(9999L).build());

        AuthorFacade facadeMock = mock(AuthorFacade.class);
        when(facadeMock.findAll()).thenReturn(expectedResults);

        Whitebox.setInternalState(authorManager, "authorFacade", facadeMock);

        List<Author> actual = authorManager.getAllAuthors();

        verify(facadeMock, times(1)).findAll();
        Assert.assertEquals(actual, expectedResults);
    }

    @Test
    public void testSave() throws Exception {
        AuthorHome homeMock = mock(AuthorHome.class);
        Whitebox.setInternalState(authorManager, "authorHome", homeMock);

        Author authorParam = Author.builder().build();
        authorManager.save(authorParam);

        verify(homeMock, times(1)).insert(authorParam);
    }

    @Test
    public void testUpdate() throws Exception {
        AuthorHome homeMock = mock(AuthorHome.class);
        Whitebox.setInternalState(authorManager, "authorHome", homeMock);

        Author authorParam = Author.builder().build();
        authorManager.update(authorParam);

        verify(homeMock, times(1)).update(authorParam);
    }

    @Test
    public void testDelete() throws Exception {
        AuthorHome homeMock = mock(AuthorHome.class);
        Whitebox.setInternalState(authorManager, "authorHome", homeMock);

        Long deleteParam = 100L;
        authorManager.delete(deleteParam);

        verify(homeMock, times(1)).deleteByPk(deleteParam);
    }

    @Test
    public void testDeleteList() throws Exception {
        AuthorHome homeMock = mock(AuthorHome.class);
        Whitebox.setInternalState(authorManager, "authorHome", homeMock);

        ArrayList<Long> deleteParams = new ArrayList<>();
        deleteParams.add(234L);
        deleteParams.add(189L);
        deleteParams.add(111L);
        deleteParams.add(9999L);
        authorManager.deleteList(deleteParams);

        verify(homeMock, times(1)).deleteByPkList(deleteParams);
    }

    @Test
    public void testGetAutocompleteBySecondName() throws Exception {
        AuthorFacade homeFacade = mock(AuthorFacade.class);
        Whitebox.setInternalState(authorManager, "authorFacade", homeFacade);

        String prefixParam = "B";
        List<Author> expected = new ArrayList<>();
        expected.add(Author.builder().secondName("Bolduin").build());
        expected.add(Author.builder().secondName("Belmondo").build());

        when(homeFacade.getAutocompleteByColumn(prefixParam)).thenReturn(expected);

        List<Author> actual = authorManager.getAutocompleteBySecondName(prefixParam);

        verify(homeFacade, times(1)).getAutocompleteByColumn(prefixParam);
        Assert.assertEquals(actual, expected);
    }
}